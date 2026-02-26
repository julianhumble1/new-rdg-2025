import { Label, TextInput } from "flowbite-react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import UserService from "../services/UserService.js";
import ErrorMessage from "./modals/ErrorMessage.jsx";

const ResetPassword = () => {
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMessage("");

    if (newPassword !== confirmPassword) {
      setErrorMessage("Passwords do not match.");
      return;
    }

    try {
      await UserService.updateOwnPassword(newPassword);
      toast.success("Password updated successfully.");
      navigate("/archive/dashboard");
    } catch (error) {
      setErrorMessage("Failed to update password. Please try again.");
    }
  };

  return (
    <div className="flex justify-center">
      <form
        onSubmit={handleSubmit}
        className="flex lg:w-1/2 w-full flex-col gap-4 min-w-1/2 border-4 p-4 m-4 rounded-xl border-sky-900 border-opacity-60 bg-slate-200"
      >
        <div className="text-2xl font-bold">Update Your Password</div>
        <p className="text-gray-700">
          You must update your password before continuing.
        </p>
        <div>
          <div className="mb-2">
            <Label value="New Password" />
          </div>
          <TextInput
            placeholder="New password"
            type="password"
            required
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
          />
        </div>
        <div>
          <div className="mb-2">
            <Label value="Confirm New Password" />
          </div>
          <TextInput
            placeholder="Confirm new password"
            type="password"
            required
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
          />
        </div>
        <button
          type="submit"
          className="bg-green-700 hover:bg-green-800 text-white p-2 rounded transition"
        >
          Update Password
        </button>
        <ErrorMessage message={errorMessage} />
      </form>
    </div>
  );
};

export default ResetPassword;
