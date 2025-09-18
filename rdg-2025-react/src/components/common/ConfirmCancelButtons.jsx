const ConfirmCancelButtons = ({ handleCancel, customSubmitText }) => {
  return (
    <div className="grid grid-cols-2 justify-end px-2">
      <button
        className="text-sm hover:underline font-bold text-center col-span-1"
        onClick={handleCancel}
        type="button"
      >
        Cancel
      </button>
      <button
        className="hover:underline bg-sky-900  p-2 py-1 rounded w-full text-white col-span-1 text-sm"
        // onClick={handleSubmit}
      >
        {customSubmitText ?? "Submit"}
      </button>
    </div>
  );
};

export default ConfirmCancelButtons;
