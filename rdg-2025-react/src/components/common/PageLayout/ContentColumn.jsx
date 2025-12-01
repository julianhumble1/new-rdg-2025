const ContentColumn = ({ title, content }) => {
  const Content = content;
  return (
    <div className="flex-1 flex flex-col md:p-6 pt-0 gap-3 p-4">
      <div className="text-[50px] text-rdg-blue tracking-wider">{title}</div>
      <Content />
    </div>
  );
};

export default ContentColumn;
