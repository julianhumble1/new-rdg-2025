import DatePicker from "react-datepicker";
import { Label } from "flowbite-react";

const isMidnight = (date) =>
  date.getHours() === 0 && date.getMinutes() === 0 && date.getSeconds() === 0;

// Combines a date-only picker with an optional time-only picker so a time
// is never assumed - the value stays at midnight unless a time is explicitly chosen.
const OptionalDateTimePicker = ({ label, value, onChange }) => {
  const dateOnly = value || null;
  const timeOnly = value && !isMidnight(value) ? value : null;

  const handleDateChange = (date) => {
    if (!date) {
      onChange(null);
      return;
    }
    const combined = new Date(date);
    if (timeOnly) {
      combined.setHours(timeOnly.getHours(), timeOnly.getMinutes(), 0, 0);
    } else {
      combined.setHours(0, 0, 0, 0);
    }
    onChange(combined);
  };

  const handleTimeChange = (time) => {
    if (!value) return;
    const combined = new Date(value);
    if (time) {
      combined.setHours(time.getHours(), time.getMinutes(), 0, 0);
    } else {
      combined.setHours(0, 0, 0, 0);
    }
    onChange(combined);
  };

  return (
    <div className="flex flex-col gap-2">
      <div>
        <div className="italic">
          <Label value={label} />
        </div>
        <DatePicker
          className="border border-gray-300 rounded p-2 text-sm w-full"
          selected={dateOnly}
          onChange={handleDateChange}
          dateFormat="dd/MM/yyyy"
          isClearable
          showIcon
        />
      </div>
      <div>
        <div className="italic">
          <Label value="Time (optional)" />
        </div>
        <DatePicker
          className="border border-gray-300 rounded p-2 text-sm w-full"
          selected={timeOnly}
          onChange={handleTimeChange}
          showTimeSelect
          showTimeSelectOnly
          timeIntervals={15}
          timeCaption="Time"
          dateFormat="h:mm aa"
          placeholderText="No time set"
          isClearable
          disabled={!dateOnly}
        />
      </div>
    </div>
  );
};

export default OptionalDateTimePicker;
