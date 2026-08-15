export default class RoleHelper {
  static isAdmin = (role) => Boolean(role) && role.includes("ADMIN");

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
