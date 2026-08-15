import EditorJS from "@editorjs/editorjs";
import Header from "@editorjs/header";
import { useEffect, useRef } from "react";
import { toast } from "react-toastify";
import PageContentService from "../../../services/PageContentService.js";
import Button from "../Button.jsx";
import {
  ColorBlackInlineTool,
  ColorBlueInlineTool,
  ColorGreyInlineTool,
  ColorRedInlineTool,
  ColorYellowInlineTool,
} from "./ColorInlineTool.js";

const EMPTY_DATA = {
  blocks: [
    {
      type: "paragraph",
      data: {
        text: "",
      },
    },
  ],
};

const INLINE_TOOLBAR = [
  "bold",
  "italic",
  "link",
  "colorRed",
  "colorBlue",
  "colorYellow",
  "colorBlack",
  "colorGrey",
];

const TextEditor = ({ contentKey, initialData, onSaved, onCancel }) => {
  const ejInstance = useRef(null);
  const containerRef = useRef(null);

  useEffect(() => {
    const holderEl = document.createElement("div");
    containerRef.current.appendChild(holderEl);

    const editor = new EditorJS({
      holder: holderEl,
      autofocus: true,
      data: initialData ?? EMPTY_DATA,
      inlineToolbar: INLINE_TOOLBAR,
      tools: {
        header: {
          class: Header,
          inlineToolbar: INLINE_TOOLBAR,
          config: {
            levels: [2, 3, 4],
            defaultLevel: 2,
          },
        },
        colorRed: ColorRedInlineTool,
        colorBlue: ColorBlueInlineTool,
        colorYellow: ColorYellowInlineTool,
        colorBlack: ColorBlackInlineTool,
        colorGrey: ColorGreyInlineTool,
      },
    });

    ejInstance.current = editor;

    return () => {
      holderEl.remove();
      editor.isReady
        .then(() => editor.destroy())
        .catch((e) => console.error(e));
      ejInstance.current = null;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleSave = async () => {
    try {
      const outputData = await ejInstance.current.save();
      await PageContentService.saveContent(outputData, contentKey);
      toast.success("Content saved.");
      onSaved?.();
    } catch (e) {
      toast.error(e.message);
    }
  };

  const handleCancel = () => {
    onCancel?.();
  };

  return (
    <div className="flex flex-col gap-3">
      <div ref={containerRef} className="bg-slate-100 rounded-2xl p-2"></div>
      <div className="flex gap-2 justify-end">
        <div className="w-32">
          <Button onClick={handleCancel}>Cancel</Button>
        </div>
        <div className="w-32">
          <Button onClick={handleSave}>Save</Button>
        </div>
      </div>
    </div>
  );
};

export default TextEditor;
