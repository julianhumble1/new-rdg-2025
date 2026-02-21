import { useState } from "react";
import AlertDialog from "../library/AlertDialog.jsx";
import UserService from "../../services/UserService.js";
import { generate } from "generate-password-browser";
import { ClipboardWithIconText } from "flowbite-react";

const ResetPasswordDialog = ({ username, id }) => {
  const [generatedPassword, setGeneratedPassword] = useState("");
  const [showRevealDialog, setShowRevealDialog] = useState(false);

  return (
    <div className="flex flex-col gap-2">
      <div className="flex gap-2">
        <AlertDialog
          trigger={
            <button className="text-black hover:underline font-bold">
              Reset Password
            </button>
          }
          title="Reset Password?"
          description={`Are you sure you want to reset the password for user ${username} ? This cannot be undone.`}
          cancelText="No, cancel password reset"
          actionText="Yes, reset password"
          actionColor="bg-rdg-blue hover:ring-2"
          onAction={async () => {
            const newPassword = generate({ length: 20, symbols: true });
            setGeneratedPassword(newPassword); // store to reveal in separate dialog
            await UserService.updatePassword(newPassword, id);
            setShowRevealDialog(true); // open reveal dialog instead of inline table UI
          }}
          onCancel={() => {
            /* handle cancel */
          }}
        />
      </div>

      {/* Reveal dialog - uses the same AlertDialog component but controlled */}
      <AlertDialog
        open={showRevealDialog}
        onOpenChange={setShowRevealDialog}
        title="Save this new password now: "
        description={null}
        cancelText="Close"
        actionText={null}
        onCancel={() => setShowRevealDialog(false)}
      >
        <div className="flex items-start justify-between gap-1 flex-col">
          <div className="relative w-full mt-1">
            <label htmlFor="npm-install" className="sr-only">
              Label
            </label>
            <input
              id={generatedPassword}
              type="text"
              className="col-span-6 block w-full rounded-lg border border-gray-300 bg-gray-50 px-2.5 py-4 text-sm text-gray-500 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-gray-400 dark:placeholder:text-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
              value={generatedPassword}
              disabled
              readOnly
            />
            <ClipboardWithIconText valueToCopy={generatedPassword} />
          </div>
          <div className="text-xs text-gray-500 mt-1">
            You will not be able to see this again.
          </div>
        </div>
      </AlertDialog>

      {/* existing inline table/UI removed — handled by reveal dialog */}
    </div>
  );
};

export default ResetPasswordDialog;
