import { NoSymbolIcon } from "@heroicons/react/16/solid";

const NotFound = () => {
  return (
    <div className="flex justify-center flex-col h-full my-auto">
      <div className="flex justify-center">
        <div className="flex flex-col">
          <NoSymbolIcon className="inline h-24 w-24 mx-auto text-sky-900" />
          <div className="mx-auto italic text-xl font-bold text-sky-900">
            Not Found
          </div>
        </div>
      </div>
    </div>
  );
};

export default NotFound;
