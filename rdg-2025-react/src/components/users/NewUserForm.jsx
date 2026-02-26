import { Label, TextInput } from "flowbite-react";
import ContentCard from "../common/ContentCard.jsx";
import { useState } from "react";
import ConfirmCancelButtons from "../common/ConfirmCancelButtons.jsx";
import { useNavigate } from "react-router-dom";
import Select from "react-select";
import UserService from "../../services/UserService.js";
import PasswordRevealDialog from "./PasswordRevealDialog.jsx";

const NewUserForm = () => {
  const roleOptions = [
    { value: "admin", label: "Admin" },
    { value: "superadmin", label: "Super Admin" },
  ];

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [role, setRole] = useState(roleOptions[0]);

  const [showPasswordDialog, setShowPasswordDialog] = useState(false);

  const navigate = useNavigate();

  const handleCancel = () => {
    navigate("/archive/dashboard");
  };

  const handleSubmit = (e) => {
    e.preventDefault()
    setShowPasswordDialog((prev) => !prev)
  }

  const handleCreate = async (e) => {
    e.preventDefault();
    const response = await UserService.createUser(
      name,
      email,
      role.value,
      "password",
    );
    console.log(response);
  };

  const disableSubmit = !name || !role || !email;

  return (
    <>
      <ContentCard>
        <div className="pb-3 font-bold text-xl">Create Admin User</div>
        <form className="flex flex-col gap-2 max-w-md" onSubmit={handleSubmit}>
          <div>
            <div className="mb-2 block italic">
              <Label value="Name (required)" />
            </div>
            <TextInput
              placeholder="John Doe"
              required
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
          </div>
          <div>
            <div className="mb-2 block italic">
              <Label value="Email (required)" />
            </div>
            <TextInput
              placeholder="example@email.com"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>
          <div>
            <div className="mb-2 block italic">
              <Label value="Role (required)" />
            </div>
            <Select
              options={roleOptions}
              className="text-sm"
              styles={{
                control: (baseStyles) => ({
                  ...baseStyles,
                  borderRadius: 8,
                  padding: 1,
                }),
              }}
              value={role}
              onChange={setRole}
            />
          </div>
          <ConfirmCancelButtons
            handleCancel={handleCancel}
            disableSubmit={disableSubmit}
          />
        </form>
      </ContentCard>
      <PasswordRevealDialog
        showPasswordDialog={showPasswordDialog}
        setShowPasswordDialog={setShowPasswordDialog}
        name={name}
        email={email}
        role={role}
      />
    </>
  );
};

export default NewUserForm;
