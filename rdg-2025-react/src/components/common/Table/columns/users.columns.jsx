import { Bars3Icon } from "@heroicons/react/16/solid";
import { Dropdown, DropdownItem } from "flowbite-react";

export const usersColumns = [
  {
    name: "Username",
    selector: (row) => row.username,
  },
  {
    name: "Email",
    selector: (row) => row.email,
  },
  {
    name: "Actions",
    cell: (row) => (
      <Dropdown className="w-34" renderTrigger={() => (
    <button className="bg-rdg-blue/80 text-white rounded-full p-2"><Bars3Icon className="w-4 h-4 " /></button>
  )}>
        <DropdownItem >Reset Password</DropdownItem>
      </Dropdown>
    ),
  },
];
