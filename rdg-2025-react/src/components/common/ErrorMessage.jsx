const ErrorMessage = ({ icon, message }) => {
  return (
    <div className="flex justify-center flex-col h-full my-auto ">
      <div className="flex justify-center">
        <div className="flex flex-col">
          <div className="inline h-24 w-24 mx-auto text-sky-900">{icon}</div>
          <div className="mx-auto italic text-xl font-bold text-sky-900">
            {message}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ErrorMessage;
