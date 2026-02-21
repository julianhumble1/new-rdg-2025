import {
  Root as AlertDialogRoot,
  Trigger as AlertDialogTrigger,
  Portal as AlertDialogPortal,
  Overlay as AlertDialogOverlay,
  Content as AlertDialogContent,
  Title as AlertDialogTitle,
  Description as AlertDialogDescription,
  Cancel as AlertDialogCancel,
  Action as AlertDialogAction,
} from "@radix-ui/react-alert-dialog";

/**
 * Props:
 * - trigger (ReactNode): The button or element that opens the dialog. Optional if `open` is controlled.
 * - open (boolean): Controlled open state (optional).
 * - onOpenChange (fn): Controlled open change handler (optional).
 * - title, description, cancelText, actionText, onAction, onCancel, actionColor, children
 */
const AlertDialog = ({
  trigger,
  open,
  onOpenChange,
  title = "Are you absolutely sure?",
  description = "This action cannot be undone.",
  cancelText = "Cancel",
  actionText = "Confirm",
  onAction,
  onCancel,
  actionColor = "bg-red-600 hover:bg-red-700 focus:ring-red-400",
  children,
}) => (
  <AlertDialogRoot {...(open !== undefined ? { open, onOpenChange } : {})}>
    {trigger ? (
      <AlertDialogTrigger asChild>{trigger}</AlertDialogTrigger>
    ) : null}
    <AlertDialogPortal>
      <AlertDialogOverlay className="fixed inset-0 bg-black bg-opacity-40" />
      <AlertDialogContent className="fixed left-1/2 top-1/2 max-h-[85vh] w-[90vw] max-w-[500px] -translate-x-1/2 -translate-y-1/2 rounded-md bg-white p-6 shadow-lg focus:outline-none">
        <AlertDialogTitle className="m-0 text-lg font-semibold text-gray-900">
          {title}
        </AlertDialogTitle>
        {description ? (
          <AlertDialogDescription className="mb-5 mt-4 text-base leading-normal text-gray-700">
            {description}
          </AlertDialogDescription>
        ) : null}
        {children}
        <div className="flex justify-end gap-6 mt-4">
          {cancelText !== null ? (
            <AlertDialogCancel asChild>
              <button
                className="inline-flex h-[35px] items-center justify-center rounded bg-gray-200 px-4 font-medium text-gray-700 hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-400 select-none"
                onClick={onCancel}
                type="button"
              >
                {cancelText}
              </button>
            </AlertDialogCancel>
          ) : null}
          {actionText !== null ? (
            <AlertDialogAction asChild>
              <button
                className={`inline-flex h-[35px] items-center justify-center rounded px-4 font-medium text-white focus:outline-none focus:ring-2 select-none ${actionColor}`}
                onClick={onAction}
                type="button"
              >
                {actionText}
              </button>
            </AlertDialogAction>
          ) : null}
        </div>
      </AlertDialogContent>
    </AlertDialogPortal>
  </AlertDialogRoot>
);

export default AlertDialog;
