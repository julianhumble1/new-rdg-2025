import {
  AdjustmentsHorizontalIcon,
  MagnifyingGlassIcon,
} from "@heroicons/react/16/solid";
import { TextInput } from "flowbite-react";

const FiltersTable = ({ filters }) => {
  return (
    <div className="flex flex-col bg-slate-300 min-h-screen rounded-2xl m-2 p-4 gap-2">
      <div className="font-bold text-xl flex justify-between">
        <div>Filters</div>
        <AdjustmentsHorizontalIcon className="w-6 h-6 opacity-45 hover:opacity-100" />
      </div>
      {filters.map((filter, index) => (
        <TextInput
          icon={MagnifyingGlassIcon}
          placeholder={filter}
          key={index}
        />
      ))}
    </div>
  );
};

export default FiltersTable;
