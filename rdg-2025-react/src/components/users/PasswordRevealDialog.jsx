import { useState, useEffect } from "react";
import { ClipboardWithIconText } from "flowbite-react";
import AlertDialog from "../library/AlertDialog.jsx";
import { generate } from "generate-password-browser";
import UserService from "../../services/UserService.js";

const PasswordRevealDialog = ({
  showPasswordDialog,
  setShowPasswordDialog,
  name,
  email,
  role,
}) => {
  const [generatedPassword, setGeneratedPassword] = useState("");
  const [status, setStatus] = useState("idle"); // 'idle' | 'loading' | 'success' | 'error'
  const [errorMessage, setErrorMessage] = useState("");

  useEffect(() => {
    if (showPasswordDialog) {
      setGeneratedPassword(generate({ length: 20, symbols: true }));
      setStatus("idle");
      setErrorMessage("");
    }
  }, [showPasswordDialog]);

  const handleCreate = async () => {
    setStatus("loading");
    try {
      await UserService.createUser(name, email, role.value, generatedPassword);
      setStatus("success");
    } catch (err) {
      setErrorMessage(err?.message || "Something went wrong. Please try again.");
      setStatus("error");
    }
  };

  const handleClose = () => {
    setShowPasswordDialog(false);
  };

  const handleOpenChange = (open) => {
    if (!open && status !== "loading") {
      setShowPasswordDialog(false);
    }
  };

  const passwordInput = (
    <div className="relative w-full mt-1">
      <label htmlFor="generated-password" className="sr-only">
        Generated password
      </label>
      <input
        id="generated-password"
        type="text"
        className="col-span-6 block w-full rounded-lg border border-gray-300 bg-gray-50 px-2.5 py-4 text-sm text-gray-500 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-gray-400 dark:placeholder:text-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
        value={generatedPassword}
        disabled
        readOnly
      />
      <ClipboardWithIconText valueToCopy={generatedPassword} />
    </div>
  );

  if (status === "success") {
    return (
      <AlertDialog
        open={showPasswordDialog}
        onOpenChange={handleOpenChange}
        title="User created successfully"
        description="Copy the password before closing — you will not be able to see it again."
        cancelText={null}
        actionText="Close"
        onAction={handleClose}
        actionColor="bg-green-600 hover:bg-green-700 focus:ring-green-400"
      >
        {passwordInput}
      </AlertDialog>
    );
  }

  return (
    <AlertDialog
      open={showPasswordDialog}
      onOpenChange={handleOpenChange}
      title="Confirm you want to create this user with this password"
      description="Save this password now. You will not be able to see it again."
      actionText={status === "loading" ? "Creating..." : "Create"}
      onCancel={handleClose}
      onAction={handleCreate}
      actionColor="bg-rdg-blue"
      preventActionClose
    >
      {status === "error" && (
        <p className="text-red-600 text-sm mb-2">{errorMessage}</p>
      )}
      {passwordInput}
    </AlertDialog>
  );
};

export default PasswordRevealDialog;
