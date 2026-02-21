import ResetPasswordDialog from "../../../users/ResetPasswordDialog.jsx";

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
    cell: (row) => <ResetPasswordDialog username={row.username} id={row.id} />,
  },
];
