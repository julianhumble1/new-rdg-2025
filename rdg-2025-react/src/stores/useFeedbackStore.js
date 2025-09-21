import { create } from "zustand";

const useFeedbackStore = create((set) => {
  // keep timeout id out of the zustand state to avoid store updates
  // on every timeout change (which can trigger re-render loops)
  let timeoutId = null;

  return {
    visible: false,
    message: "",
    variant: "info", // "info" | "success" | "error" | "warning"

    showFeedback: (message, variant = "info", ttl = 5000) => {
      // clear existing timeout
      if (timeoutId) {
        clearTimeout(timeoutId);
        timeoutId = null;
      }

      set({ message, variant, visible: true });

      timeoutId = setTimeout(() => {
        set({ visible: false });
        timeoutId = null;
      }, ttl);
    },

    hideFeedback: () => {
      if (timeoutId) {
        clearTimeout(timeoutId);
        timeoutId = null;
      }
      set({ visible: false });
    },
  };
});

export default useFeedbackStore;
