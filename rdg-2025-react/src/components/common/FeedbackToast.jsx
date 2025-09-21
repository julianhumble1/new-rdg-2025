import useFeedbackStore from "../../stores/useFeedbackStore.js";

const FeedbackToast = () => {
  // select individual values to avoid recreating an object each render
  const visible = useFeedbackStore((s) => s.visible);
  const message = useFeedbackStore((s) => s.message);
  const variant = useFeedbackStore((s) => s.variant);
  const hideFeedback = useFeedbackStore((s) => s.hideFeedback);

  if (!visible) return null;

  const bg =
    variant === "success"
      ? "bg-green-600"
      : variant === "error"
        ? "bg-red-600"
        : variant === "warning"
          ? "bg-yellow-600"
          : "bg-blue-600";

  return (
    <div
      className={`fixed bottom-5 right-5 z-50 ${bg} text-white rounded shadow-lg`}
    >
      <div className="px-4 py-3 max-w-lg">
        <div className="flex items-start gap-3">
          <div className="flex-1">
            <div className="font-medium">{message}</div>
          </div>
          <button
            onClick={hideFeedback}
            className="ml-2 opacity-90 hover:opacity-100"
            aria-label="Close"
          >
            ✕
          </button>
        </div>
      </div>
    </div>
  );
};

export default FeedbackToast;
