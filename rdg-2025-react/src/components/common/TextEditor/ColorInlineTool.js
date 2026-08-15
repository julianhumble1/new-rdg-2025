const TAG = "SPAN";

const makeColorInlineTool = ({ className, label, swatch }) => {
  return class ColorInlineTool {
    static get isInline() {
      return true;
    }

    static get title() {
      return label;
    }

    static get sanitize() {
      return {
        span: {
          class: (value) => value === className,
        },
      };
    }

    constructor({ api }) {
      this.api = api;
      this.button = null;
      this.state = false;
    }

    render() {
      this.button = document.createElement("button");
      this.button.type = "button";
      this.button.classList.add(this.api.styles.inlineToolButton);

      const swatchEl = document.createElement("span");
      swatchEl.style.display = "inline-block";
      swatchEl.style.width = "12px";
      swatchEl.style.height = "12px";
      swatchEl.style.borderRadius = "9999px";
      swatchEl.style.border = "1px solid rgba(0,0,0,0.2)";
      swatchEl.style.backgroundColor = swatch;
      this.button.appendChild(swatchEl);

      return this.button;
    }

    surround(range) {
      if (this.state) {
        this.unwrap(range);
        return;
      }
      this.wrap(range);
    }

    wrap(range) {
      const selectedText = range.extractContents();
      const span = document.createElement(TAG);
      span.classList.add(className);
      span.appendChild(selectedText);
      range.insertNode(span);
      this.api.selection.expandToTag(span);
    }

    unwrap(range) {
      const span = this.api.selection.findParentTag(TAG, className);
      if (!span) return;
      const text = range.extractContents();
      span.replaceWith(text);
    }

    checkState() {
      const span = this.api.selection.findParentTag(TAG, className);
      this.state = Boolean(span);
      if (this.button) {
        this.button.classList.toggle(this.api.styles.inlineToolButtonActive, this.state);
      }
      return this.state;
    }
  };
};

export const ColorRedInlineTool = makeColorInlineTool({
  className: "text-rdg-red",
  label: "Red",
  swatch: "#e9462f",
});

export const ColorBlueInlineTool = makeColorInlineTool({
  className: "text-rdg-blue",
  label: "Blue",
  swatch: "#0347f7",
});

export const ColorYellowInlineTool = makeColorInlineTool({
  className: "text-rdg-yellow",
  label: "Yellow",
  swatch: "#f7c938",
});

export const ColorBlackInlineTool = makeColorInlineTool({
  className: "text-black",
  label: "Black",
  swatch: "#000000",
});

export const ColorGreyInlineTool = makeColorInlineTool({
  className: "text-gray-500",
  label: "Grey",
  swatch: "#6b7280",
});
