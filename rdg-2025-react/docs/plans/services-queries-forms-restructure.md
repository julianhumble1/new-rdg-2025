# Services, queries and forms restructure

**Status:** Draft
**Date:** 2026-10-24
**Scope:** `src/services`, `src/hooks`, every `*Form.jsx`, the archive detail pages, and the TypeScript setup

---

## 1. Summary

The archive's forms each keep their own state, render their own field markup and do their own validation (when there is any). The data layer is only partly on React Query: lists use it, detail pages don't. This plan replaces that with clear layers:

```
service  →  query module  →  form module + form hook  →  component (built from field components)
```

TypeScript comes in only for the new layers (services, queries, forms, types, field components). Existing JSX keeps working unchanged next to them. This is not a TypeScript migration. Nothing has to be converted before this work starts.

Two follow-on features depend on this work:

- **Server-side pagination** needs the query layer (parameterised list keys) and a shared entity select.
- **The production builder wizard** needs composable schemas, reusable field groups and a mutation that can create several entities in one go.

---

## 2. Where we are today

### 2.1 Form state

- The 17 form components hold 121 `useState` calls between them, one per field.
- New and Edit forms are near-duplicates. For example, `NewProductionForm.jsx` and `EditProductionForm.jsx` differ only in initial values and the submit target.
- Adding a field to production touches six places: both forms, `ProductionPage.handleEdit`, two mutations in `useProductions`, and two methods in `ProductionService`. All of them pass positional arguments (up to 10), so getting the argument order wrong fails silently.
- There is dead state. `setFlyerFile` is never called in either production form. `successMessage` and `errorMessage` are never set in `NewProductionForm`.

### 2.2 Field markup

- The label wrapper `<div className="mb-2 block italic"><Label/></div>` and the react-select `styles={{ control: … borderRadius: 8 … }}` are copied about 112 times across 19 files.
- The copies have drifted:
  - Some labels are italic and some aren't.
  - The description counter updates on blur in some forms and live in others.
  - Price fields format on blur by writing straight to the DOM, which leaves state and display out of step.
- `common/FormTemplate.jsx` exists but no form uses it.

### 2.3 Validation

Rules are spread across layers, and some exist only as text:

| Rule | Where it lives today |
|---|---|
| Required fields | HTML `required`, on some fields only |
| Trimming | Inside services (`ProductionService`, `VenueService`, `PersonService` …) |
| Date wire format | In `ProductionPage.handleEdit` and `NewProductionForm` |
| Max 2000 characters | In label text only, never enforced |
| Server error messages | Mapped by status code in one method (`updateProductionWithImage`) |

Example bug: `NewPerformanceForm` labels Venue "(required)" but doesn't enforce it. Submitting without a venue throws on `venue.value`. The error is written to `failMessage`, which is never rendered, so the user sees nothing happen.

### 2.4 The data layer is only half on React Query

- **Detail pages skip React Query.** `ProductionPage`, `VenuePage`, `PersonPage`, `FestivalPage` and `EventPage` call services directly in `useEffect`, hold the results in `useState`, and refetch by hand after mutations.
- **Some mutations invalidate nothing.** `usePerformances`, `useCredits` and `useAwards` have mutations but no queries, so they invalidate keys that nothing reads.
- **Several forms bypass the mutation hooks** and call services directly: the credit, award, performance and user forms.
- **No cross-entity invalidation.** Renaming a venue doesn't refresh production lists that show the venue name.
- **Services show toasts.** A flow that chains several service calls shows one toast per call.

### 2.5 Things that will block the next features

- Every entity dropdown loads the entire list through `FetchValueOptionsHelper` (all people, all productions, …). Server-side pagination breaks this.
- Positional service arguments plus toasts inside services make it awkward to compose several calls into one operation, which the wizard needs.

---

## 3. Target structure

### 3.1 Layers

| Layer | Location | Owns | Does not own |
|---|---|---|---|
| **Types** | `src/types/<entity>.ts` | `Entity` (what the API returns) and `EntityInput` (what forms produce and services accept) | Validation |
| **Service** | `src/services/<entity>.service.ts` | Endpoints, wire format (date strings, field names, `null` vs `0`), unwrapping response envelopes | Validation, caching, toasts |
| **Query module** | `src/queries/<entity>.ts` | Query keys, queries, mutations, **every invalidation a change causes** (including other entities' keys) | Validation, payload shape, toasts |
| **Form module** | `src/forms/<entity>.ts` | Zod schema, `toFormValues(entity?)`, `toPayload(values)`; trimming and other normalisation | API types beyond `EntityInput`, fetching, React |
| **Form hook** | `src/forms/use<Entity>Form.ts` | Wiring react-hook-form to the schema, submit, success toast, navigate or close on success, field behaviour (e.g. picking a production pre-fills its venue) | Layout |
| **Field components** | `src/components/form/` | Label, control, error, hint and consistent styling | Rules, payload shape |
| **Component** | `src/components/<entity>/<Entity>Form.jsx` | Which fields appear, their order and labels, UI-only state | Rules, trimming, payload shape |

Two differences from the plan used in the other repo:

- **No dirty-field tracking.** Updates send the full payload, so the form module needs no diff logic.
- **No separate page hook to start with.** Once detail data comes from queries, a page has little left to manage beyond edit mode. Add a page hook only for a page that grows busy.

### 3.2 Folder layout

```
src/
  types/            production.ts, venue.ts, …            (TS)
  services/         api.ts, production.service.ts, …      (TS)
  queries/          production.ts, venue.ts, keys.ts, …   (TS)
  forms/            production.ts, useProductionForm.ts   (TS)
  components/
    form/           Form, FormField, TextField, …         (TSX)
    productions/    ProductionForm.jsx/.tsx, ProductionPage.jsx, …
```

`src/hooks/use*.js` and the old `src/services/*Service.js` files are deleted entity by entity as each one moves across.

### 3.3 New dependencies

- `zod`
- `react-hook-form`
- `@hookform/resolvers`
- `typescript`
- `typescript-eslint` (dev)

---

## 4. TypeScript: partial introduction

### 4.1 Why not wait for a full migration

Zod and react-hook-form work in plain JS, so runtime validation doesn't need TypeScript. TypeScript adds compile-time checks in exactly the places this work creates:

- A field `name` checked against the form's values: `<TextField name="nmae">` becomes an error.
- Form output checked against the service's input type.
- Typed query data, so detail pages stop guessing at `response.data.production`.
- The wizard's nested form type (production plus arrays of performances and credits), which is where untyped code hurts most.

Vite compiles `.ts` and `.tsx` with no extra setup, and `.jsx` files can import from `.ts` files. That makes the new folders a natural boundary.

### 4.2 The boundary

| TypeScript (required) | TypeScript (recommended) | Stays JS |
|---|---|---|
| `src/types`, `src/services`, `src/queries`, `src/forms`, `src/components/form` | Entity form components. They are rewritten anyway, and TSX is where the `name` checking pays off. | Pages, tables, highlights, public pages, header and footer. Convert only when touched for another reason. |

Rule of thumb: **new or fully rewritten → TS; edited → stays as it is.**

### 4.3 Setup

`tsconfig.json`:

```json
{
  "compilerOptions": {
    "target": "ES2022",
    "lib": ["ES2022", "DOM", "DOM.Iterable"],
    "module": "ESNext",
    "moduleResolution": "bundler",
    "jsx": "react-jsx",
    "strict": true,
    "allowJs": true,
    "checkJs": false,
    "noEmit": true,
    "isolatedModules": true,
    "skipLibCheck": true,
    "types": ["vite/client"]
  },
  "include": ["src"]
}
```

Other changes:

- `src/vite-env.d.ts` declares `ImportMetaEnv` (`VITE_BACKEND_URL`, `VITE_CLOUDINARY_BASE_FOLDER`).
- Add `"typecheck": "tsc --noEmit"` to `package.json`.
- Extend `eslint.config.js` with `typescript-eslint` for `**/*.{ts,tsx}`.
- `strict: true` from the start. It costs almost nothing on new code and is painful to turn on later.

### 4.4 Types come from schemas where possible

```ts
// forms/production.ts
export type ProductionFormValues = z.input<typeof productionSchema>;
// types/production.ts — hand-written, because it describes the API response, not a form
export interface Production { id: number; name: string; venue: VenueSummary | null; … }
export interface ProductionInput { name: string; venueId: number | null; auditionDate: Date | null; … }
```

`EntityInput` is written by hand rather than inferred from the schema. Services and the wizard depend on it, and a small schema tweak shouldn't silently change the service contract. `toPayload` is the single place that maps one to the other, and the type checker enforces that mapping.

---

## 5. Layer details

### 5.1 Services

One axios instance replaces the per-method token and header code:

```ts
// services/api.ts
export const api = axios.create({ baseURL: import.meta.env.VITE_BACKEND_URL });

api.interceptors.request.use((config) => {
    const token = Cookies.get("token");
    if (token) config.headers.Authorization = `Bearer ${token}`;
    return config;
});

api.interceptors.response.use(undefined, (error) => Promise.reject(toApiError(error)));
```

`toApiError` converts axios errors into one shape: `{ status, message, fieldErrors? }`. This replaces the status-code mapping currently found in one method.

Services take one object argument, encode it for the wire and unwrap the response:

```ts
// services/production.service.ts
export const productionService = {
    list: async (): Promise<Production[]> => (await api.get("/productions")).data.productions,
    get: async (id: number): Promise<ProductionDetail> => (await api.get(`/productions/${id}`)).data,
    create: async (input: ProductionInput): Promise<Production> =>
        (await api.post("/productions", toWire(input))).data.production,
    update: async (id: number, input: ProductionInput): Promise<Production> =>
        (await api.patch(`/productions/${id}`, toWire(input))).data.production,
    remove: (id: number) => api.delete(`/productions/${id}`),
};

const toWire = (input: ProductionInput) => ({
    ...input,
    auditionDate: input.auditionDate ? format(input.auditionDate, "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS") : "",
});
```

Services contain no `toast`, no `.trim()` and no `try/catch` that only re-throws.

`CloudinaryService` moves to `cloudinary.service.ts` on the same pattern.

### 5.2 Query modules

**Key factories.** Each entity gets a key factory, so related queries share a prefix and can be invalidated together:

```ts
export const productionKeys = {
    all: ["productions"] as const,
    lists: () => [...productionKeys.all, "list"] as const,
    list: (params: ListParams = {}) => [...productionKeys.lists(), params] as const,
    details: () => [...productionKeys.all, "detail"] as const,
    detail: (id: number) => [...productionKeys.details(), id] as const,
    options: (search = "") => [...productionKeys.all, "options", search] as const,
};
```

**Queries and mutations** are exported as separate hooks rather than one `useProductions()` that returns everything. Components then subscribe only to what they use.

```ts
export const useProduction = (id: number) =>
    useQuery({ queryKey: productionKeys.detail(id), queryFn: () => productionService.get(id), staleTime: TEN_MINUTES });

export const useUpdateProduction = () => {
    const qc = useQueryClient();
    return useMutation({
        mutationFn: ({ id, input }: { id: number; input: ProductionInput }) => productionService.update(id, input),
        onSuccess: () => invalidateAll(qc, [productionKeys.all, venueKeys.details(), personKeys.details()]),
    });
};
```

**Detail pages** switch from `useEffect` + `useState` to `useProduction(id)`. The manual `fetchProductionData()` calls after deletes and edits go away because invalidation handles them.

**Invalidation policy:**

- **The entity's own type:** invalidate `keys.all`.
- **Another type that displays this entity** (e.g. productions showing a venue name): invalidate that type's `all`. Finding the exact cached queries that reference one entity would mean searching the cache, which isn't worth it. `invalidateQueries` only refetches queries that are on screen; the rest are marked stale.
- **Parents whose IDs are already in the payload** (a credit knows its `productionId` and `personId`): invalidate those specific `detail(id)` keys. If that ever gets fiddly, fall back to `all`.

**Invalidation map.** Still to verify: check each against what the pages actually display.

| Mutation | Invalidates |
|---|---|
| Venue create/update/delete | `venueKeys.all`, `productionKeys.all`, `performanceKeys.all` |
| Production create/update/delete | `productionKeys.all`, `venueKeys.details()`, `personKeys.details()`, `festivalKeys.details()` |
| Performance create/update/delete | `performanceKeys.all`, `productionKeys.detail(productionId)`, `venueKeys.detail(venueId)`, `festivalKeys.detail(festivalId)` |
| Credit create/delete | `creditKeys.all`, `productionKeys.detail(productionId)`, `personKeys.detail(personId)` |
| Credit update | `creditKeys.all`, `productionKeys.details()`, `personKeys.details()` (a credit can move to another person or production) |
| Award create/update/delete | `awardKeys.all`, plus details of the linked production, person and festival |
| Person update/delete | `personKeys.all`, `productionKeys.details()` (credits show names) |
| Festival / Event | Their own `all`, plus details of linked venues and productions |

The map lives in the query modules, next to each mutation. That makes the query module the one place that answers "what does changing X affect?".

### 5.3 Forms

#### Form module (pure, no React)

```ts
// forms/production.ts
export const productionSchema = z.object({
    name: z.string().trim().min(1, "Name is required"),
    venueId: z.number().nullable(),
    author: z.string().trim(),
    description: z.string().trim().max(2000, "Maximum 2000 characters"),
    auditionDate: z.date().nullable(),
    sundowners: z.boolean(),
    notConfirmed: z.boolean(),
    flyerImage: z.instanceof(File).nullable(),
});

export type ProductionFormValues = z.input<typeof productionSchema>;

export const toFormValues = (p?: Production): ProductionFormValues => ({
    name: p?.name ?? "",
    venueId: p?.venue?.id ?? null,
    author: p?.author ?? "",
    description: p?.description ?? "",
    auditionDate: p?.auditionDate ? new Date(p.auditionDate) : null,
    sundowners: p?.sundowners ?? false,
    notConfirmed: p?.notConfirmed ?? false,
    flyerImage: null,
});

export const toPayload = ({ flyerImage, ...v }: z.output<typeof productionSchema>): ProductionInput => v;
```

#### Form hook

```ts
// forms/useProductionForm.ts
export const useProductionForm = ({ production, onSaved }: { production?: Production; onSaved: (p: Production) => void }) => {
    const form = useForm({ resolver: zodResolver(productionSchema), defaultValues: toFormValues(production) });
    const save = useSaveProduction(); // creates or updates, then uploads the flyer if present

    const onSubmit = form.handleSubmit(async (values) => {
        const saved = await save.mutateAsync({ id: production?.id, input: toPayload(values), flyer: values.flyerImage });
        toast.success(`${production ? "Updated" : "Created"} ${saved.name}`);
        onSaved(saved);
    });

    return { form, onSubmit };
};
```

The flyer upload sits in the query module (`useSaveProduction`) rather than the form hook. Its invalidation then lives with the production's, and the wizard can reuse it.

#### Component

New and Edit become one component. The route or page decides which `production` (if any) to pass in and what `onSaved` does.

```jsx
const ProductionForm = ({ production, onSaved, onCancel }) => {
    const { form, onSubmit } = useProductionForm({ production, onSaved });
    return (
        <Form form={form} onSubmit={onSubmit}>
            <TextField name="name" label="Production name" placeholder="Oliver!" required />
            <EntitySelect name="venueId" entity="venue" label="Venue" clearable initialOption={production?.venue} />
            <TextField name="author" label="Author" placeholder="William Shakespeare" />
            <TextAreaField name="description" label="Description" maxLength={2000} rows={4} />
            <DateTimeField name="auditionDate" label="Audition date" optional />
            <CheckboxField name="sundowners" label="Sundowners?" />
            <ImageField name="flyerImage" label="Flyer" existingUrl={flyerUrl} />
            <FormActions onCancel={onCancel} />
        </Form>
    );
};
```

`required` on a field is visual only (it shows the marker). The rule itself lives in the schema.

#### Field component library (`src/components/form/`)

| Component | Wraps | Notes |
|---|---|---|
| `Form` | `FormProvider` + `<form>` | Sets the layout (`flex flex-col gap-2 max-w-md`) |
| `FormField` | Label, control slot, error, hint | The one place for label and error styling |
| `TextField` | Flowbite `TextInput` | |
| `TextAreaField` | Flowbite `Textarea` | Live character counter when `maxLength` is set |
| `MoneyField` | `TextInput type=number` | Stores a number and formats to 2dp on blur through form state, not the DOM |
| `CheckboxField` | Flowbite `Checkbox` | |
| `SelectField` | react-select | Static options; the stored value is the option `value`, not the `{label, value}` object |
| `EntitySelect` | react-select | `entity="venue" \| "production" \| "person" \| "festival"`; stores an ID. Gets its options from the query layer (see §7) |
| `DateTimeField` | `OptionalDateTimePicker` / react-datepicker | |
| `ImageField` | `FileInput` + preview | Shows the existing URL until a new file is picked |
| `FormActions` | Cancel + submit | Disables submit while submitting |

All fields read their value and error through `useController` / `useFormContext`, so each field takes a `name` and nothing else form-related.

**Requirement for the wizard: nested names.** Fields must work with paths like `performances.2.venueId`. react-hook-form handles this natively. Entity field groups (e.g. `PerformanceFields`) should therefore take a `prefix` prop. Then the same group renders on its own page (`prefix=""`) and inside the wizard (`prefix="performances.2."`).

### 5.4 Toasts and errors

| Concern | Owner |
|---|---|
| "Created / updated X" toast, navigating, closing edit mode | Form hook |
| Delete success toast | Delete call site (a small shared `useConfirmDelete` helper) |
| Error toast (default) | Global `MutationCache.onError` on the `QueryClient`, using `ApiError.message` |
| Server field errors (e.g. 409 "name already exists") | Form hook, using `form.setError`; sets `meta: { silent: true }` to skip the global toast |
| Query errors (failed loads) | The page's error state, not toasts |

With a global error handler, no mutation can fail silently (see the `NewPerformanceForm` bug in §2.3). The local `SuccessMessage` and `ErrorMessage` state in forms and pages is removed.

---

## 6. Delivery phases

Each entity is one PR. Phases 0 and 1 are the review point: if the pattern feels wrong on Venue, change it before rolling it out.

| Phase | Scope | Done when |
|---|---|---|
| **0. Foundations** | Dependencies, `tsconfig`, ESLint for TS, `typecheck` script, `api.ts`, `ApiError`, `MutationCache.onError`, Vitest config | `build`, `lint` and `typecheck` pass; no behaviour change |
| **1. Pilot: Venue** | Types, service, query module, form module and hook, the field components Venue needs, one merged `VenueForm`, `VenuePage` and `AllVenues` on queries | Venue new, edit, detail and delete work; old `VenueService` and `useVenues` deleted; form module tests pass; pattern reviewed |
| **2. Production** | As above, plus `ImageField`, `DateTimeField`, `EntitySelect`, `useSaveProduction` with flyer upload, `ProductionPage` on `useProduction` | `ProductionPage` holds no server data in `useState` |
| **3. Remaining entities** | Performance (with `prefix`-ready `PerformanceFields`), Credit (`CreditFields`), Person, Festival, Event, Award. Public home components (`Home`, `UpcomingProductions`, `HomeProductionSpotLight`) move to queries | No component imports from `src/services` |
| **4. Cleanup** | Delete `src/hooks`, `FormTemplate`, `FetchValueOptionsHelper`, the old services; review `CloudinaryTest` / `CloudinaryUploadTest`; update `CLAUDE.md` | No leftover old-pattern code |

**Out of scope for now:** the login, reset-password and user-management forms. They are small and auth-specific. Move `UserService` onto `api.ts` in phase 4 and convert the forms whenever convenient.

**Separate follow-on pieces:** pagination (§7) and the wizard (§8).

**Manual check per entity PR** (until tests cover more):

- Create with only the required fields, then with every field
- Edit every field and check the result on the detail page
- Delete
- Check that the related entity's page reflects the change (the invalidation map)
- Submit an invalid form and check the inline errors
- Force a server error and check the toast

---

## 7. How this enables server-side pagination

Backend (Spring):

- List endpoints accept a `Pageable` and filter parameters, e.g. `GET /productions?page=0&size=25&sort=name&name=oli`.
- Add a lightweight options endpoint per selectable entity, e.g. `GET /productions/options?search=oli`, returning `{ id, name }[]` capped at about 20 results.

Frontend:

- **Service:** `list(params)` maps Spring's `Page` response to `{ items, total }`.
- **Query:** `useProductionList(params)` with key `productionKeys.list(params)` and `placeholderData: keepPreviousData`, so the table doesn't flash empty between pages. Mutations already invalidate the `lists()` prefix, which covers every cached page.
- **Table:** react-data-table-component's `paginationServer`, `paginationTotalRows`, `onChangePage` and `onChangeRowsPerPage`.
- **Filters:** `useTableFilters` currently filters the full array in the browser. It becomes filter state, ideally in URL search params so filtered views can be linked, which is debounced and passed into `params`.
- **Dropdowns:** `EntitySelect` switches from "load everything" to `useQuery(productionKeys.options(debouncedSearch))`. Because every form uses `EntitySelect`, this is one change, not ten.
  - In edit mode the current value may not be in the first page of options. `EntitySelect` takes an `initialOption` (e.g. `production.venue`) so the existing label always shows.

---

## 8. How this enables the production builder wizard

**Form shape.** One react-hook-form instance, with a schema built from the entity schemas:

```ts
export const productionWizardSchema = z.object({
    production: productionSchema,
    performances: z.array(performanceSchema.omit({ productionId: true })),
    credits: z.array(creditSchema.omit({ productionId: true })),
});
```

**Steps:** Production details → Performances → Credits → Flyer → Review.

- Performances and credits are `useFieldArray` lists that render `PerformanceFields` and `CreditFields` with a `prefix`.
- Each step validates only its own fields with `form.trigger("performances")` before moving on.
- The review step shows `toPayload` output for everything that will be created.

**Submitting.** A `useCreateProductionBundle` mutation in `queries/productionBundle.ts`. It invalidates `productionKeys.all`, `performanceKeys.all`, `creditKeys.all`, `personKeys.details()` and `venueKeys.details()` once at the end. The wizard's form hook shows one success toast and navigates to the new production.

**Decision needed: who handles partial failure?**

- **Option A (recommended): a backend bundle endpoint.** `POST /productions/bundle` creates everything in one transaction. Either everything is created or nothing is.
- **Option B: client orchestration.** Create the production, then the performances and credits with the new ID, then upload the flyer. If a later step fails, either roll back by deleting what was created or tell the user exactly what was saved. The form state still needs to allow retrying only the remaining steps.

**Later options:**

- Creating a new person inline from a credit row (a nested `personSchema` with an "existing or new" toggle).
- Saving a draft (persist form values to `localStorage`).

---

## 9. Testing

Vitest and Testing Library are already installed but there are no tests. The new layers make the valuable tests cheap:

- **Form modules (priority).** For each entity:
  - The schema accepts valid input and rejects each rule with the right message.
  - `toPayload(toFormValues(entity))` round-trips.
  - Trimming and empty-to-null rules hold.
- **Service wire format.** `toWire` turns dates into the backend format and handles `null`.
- **Invalidation (for the wizard and other complex mutations).** Spy on `queryClient.invalidateQueries` and assert the keys.
- **Field components.** A few render tests: shows an error, shows the required marker, the counter updates.

Don't aim for coverage numbers. Test the rules and the mappings, because that's where the bugs in §2 came from.

---

## 10. Risks and open questions

**Risks**

- **No existing tests.** Each entity PR changes behaviour that nothing checks automatically. Mitigation: form module tests plus the manual checklist in §6.
- **Behaviour changes during the move.** Trimming moves from services to schemas, dates are encoded in a different place, and "None" venue handling changes. Each needs checking against the backend.
- **A mixed JS/TS codebase for a while.** Acceptable, with the boundary rule in §4.2. Don't convert files that aren't otherwise being touched.
- **Unknown backend error format.** `toApiError` and server field errors depend on what Spring actually returns.

**Open questions**

- [ ] What does the backend expect for "no venue": `0`, `null`, or omitting the field? Today `NewProductionForm` defaults to `0`.
- [ ] Does the backend return field-level validation errors (e.g. on 400/409)? If not, should it?
- [ ] Wizard: backend bundle endpoint (Option A) or client orchestration (Option B)?
- [ ] Who builds the `Pageable` and `/options` endpoints, and when, relative to phase 3?
- [ ] Should New and Edit stay as separate routes rendering the same `<Entity>Form`, or should all edits happen inline on detail pages (as production does with `?edit=`)?
- [ ] Should Flowbite remain the base for field components long term, or is this the moment to change? (The plan assumes Flowbite stays.)