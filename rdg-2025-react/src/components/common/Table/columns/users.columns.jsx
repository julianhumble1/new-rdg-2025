import ResetPasswordDialog from "../../../users/ResetPasswordDialog.jsx";

export const usersColumns = [
  {
    name: "Name",
    selector: (row) => row.name,
    sortable: true
  },
  {
    name: "Email",
    selector: (row) => row.email,
    sortable: true
  },
  {
    name: "Permissions",
    selector: (row) => {
      const roles = row.roles.map((roleObj) => roleObj.name)
      if (roles.includes("ROLE_SUPERADMIN")) {
        return "Superadmin"
      } else {
        return "Admin"
      }
    },
    sortable: true
  },
  {
    name: "Actions",
    cell: (row) => <ResetPasswordDialog username={row.username} id={row.id} />,
  },
];
