# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
npm run dev          # Start dev server (uses .env.development)
npm run build        # Production build
npm run lint         # ESLint check
npm run format       # Prettier format
npm run preview      # Preview production build
npm run dev:production  # Dev server with production env
```

Testing libraries (Vitest, Testing Library) are installed but no tests exist yet. To run tests once written: `npx vitest`.

## Architecture

This is a React 18 SPA for Runnymede Drama Group — a theatre company's admin and public website.

### Two distinct areas

**Public pages** (`src/pages/`) — static/informational (home, about, join, contact, upcoming). These render without authentication.

**Archive/admin section** (`src/components/`) — full CRUD management behind cookie-based auth at `/archive/*`. Includes venues, productions, festivals, events, performances, people, credits, awards, and users.

### Data fetching pattern

All server state is managed with **TanStack React Query**. The pattern is:
1. `src/services/*Service.js` — static class with axios methods (Bearer token from cookie)
2. `src/hooks/use*.js` — React Query hooks wrapping service methods (`useQuery` + `useMutation`)
3. Components call the hook, never the service directly

Mutations call `queryClient.invalidateQueries` on success. `staleTime` is typically 10 minutes. React Toastify handles success/error feedback from mutations.

### Authentication

JWT token stored in JS cookie (3-day expiry). Role stored separately as `role` cookie. `RoleHelper.js` maps a roles array to a single primary role (`ROLE_SUPERADMIN` > `ROLE_ADMIN` > `ROLE_USER`). Auth state (`loggedIn`/`setLoggedIn`) lives in `App.jsx` and is passed as props — there is no auth context.

### Backend

Spring Boot API. Base URL from env var `VITE_BACKEND_URL`:
- Dev: `http://localhost:8080`
- Prod: `https://rdg-2025-spring.onrender.com`

### Cloudinary

Images are uploaded via signed requests. Backend generates the signature at `/cloudinary/signature`. `CloudinaryService.js` handles the upload flow. Public IDs follow the pattern `${baseFolder}_${preset}_${idNumber}` where `baseFolder` is `dev` or `prod` (from `VITE_CLOUDINARY_BASE_FOLDER`).

### Styling

Tailwind CSS + Flowbite React components. Brand colours defined in `tailwind.config.js`:
- `rdg-red`: #e9462f
- `rdg-blue`: #0347f7  
- `rdg-yellow`: #f7c938

Primary font: Merriweather (serif), loaded from Google Fonts in `index.html`.

Prettier config: 4-space tabs, 120-char line width.

### Layout components

`src/components/common/PageLayout/` provides `StandardPageLayout`, `ContentColumn`, and `PhotoColumn` — used across most archive detail pages for consistent two-column (content + photo) layouts.

### Page structure conventions

Detail pages (e.g., `VenuePage`, `ProductionPage`) fetch their entity by ID, display it with `StandardPageLayout`, and include nested lists of related entities. Edit/delete actions are gated by role checks using `RoleHelper`.
