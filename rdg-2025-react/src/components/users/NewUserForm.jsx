import { Label, TextInput } from "flowbite-react";
import ContentCard from "../common/ContentCard.jsx";
import { useState } from "react";

const NewUserForm = () => {
  const [name, setName] = useState("");

  const handleSubmit = () => {
    return;
  };

  return (
    <ContentCard>
      <div className="pb-3 font-bold text-xl">Create Admin User</div>
      <form className="flex flex-col gap-2 max-w-md" onSubmit={handleSubmit}>
        <div>
          <div className="mb-2 block italic">
            <Label value="Email (required)" />
          </div>
          <TextInput
            placeholder="example@email.com"
            required
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        </div>
      </form>
    </ContentCard>
  );
};

export default NewUserForm;
