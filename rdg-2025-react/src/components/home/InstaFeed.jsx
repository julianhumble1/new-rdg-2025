import BeholdWidget from "@behold/react";

const feedId = import.meta.env.VITE_BEHOLD_WIDGET_ID;

const InstaFeed = () => {
  return (
    <div className="flex-1 flex flex-col justify-start min-w-0 px-4">
      <BeholdWidget feedId={feedId} />
    </div>
  );
};

export default InstaFeed;
