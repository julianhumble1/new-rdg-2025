import { Spinner } from "flowbite-react";

const CustomSpinner = () => {
  return (
    <div className="flex justify-center h-full">
      <div className="flex flex-col justify-center">
        <Spinner className=" text-center" size="xl" color="gray" />
      </div>
    </div>
  );
};

export default CustomSpinner;
