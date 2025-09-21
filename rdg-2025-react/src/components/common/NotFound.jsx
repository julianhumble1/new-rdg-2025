import { NoSymbolIcon } from "@heroicons/react/16/solid";
import ErrorMessage from "./ErrorMessage.jsx";

const NotFound = () => {
  return <ErrorMessage icon={<NoSymbolIcon />} message={"Not Found"} />;
};

export default NotFound;
