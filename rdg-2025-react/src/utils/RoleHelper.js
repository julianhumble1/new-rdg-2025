export default class RoleHelper {
  static determineMainRole = (roles) => {
    if (roles.includes("ROLE_SUPERADMIN")) {
      return "ROLE_SUPERADMIN";
    } else if (roles.includes("ROLE_ADMIN")) {
      return "ROLE_ADMIN";
    } else {
      return "ROLE_USER";
    }
  };
}
