import ResetPasswordDialog from "../../../users/ResetPasswordDialog.jsx";

export const usersColumns = [
  {
    name: "Name",
    selector: (row) => row.name,
  },
  {
    name: "Email",
    selector: (row) => row.email,
  },
  {
    name: "Actions",
    cell: (row) => <ResetPasswordDialog username={row.username} id={row.id} />,
  },
];
