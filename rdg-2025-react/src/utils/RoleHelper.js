export default class RoleHelper {
  static determineMainRole = (roles) => {
    if (roles.includes("ROLE_ADMIN")) {
      return "ROLE_ADMIN";
    } else if (roles.includes("ROLE_MODERATOR")) {
      return "ROLE_MODERATOR";
    } else {
      return "ROLE_USER";
    }
  };
}
