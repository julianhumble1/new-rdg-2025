import { Checkbox, Label, Textarea, TextInput, FileInput } from "flowbite-react";
import CloudinaryService from "../../services/CloudinaryService.js";
import { useState, useEffect } from "react";
import Select from "react-select";
import FetchValueOptionsHelper from "../../utils/FetchValueOptionsHelper.js";
import ErrorMessage from "../modals/ErrorMessage.jsx";
import ConfirmCancelButtons from "../common/ConfirmCancelButtons.jsx";
import { useVenues } from "../../hooks/useVenues.js";
import CustomSpinner from "../common/CustomSpinner.jsx";
import OptionalDateTimePicker from "../common/OptionalDateTimePicker.jsx";

const EditProductionForm = ({ productionData, handleEdit, setEditMode }) => {
  const { venues } = useVenues();
  const venueOptions = venues.data
    ? FetchValueOptionsHelper.formatVenueOptions(venues.data)
    : [];

  const [name, setName] = useState(productionData.name);
  const [venue, setVenue] = useState(
    productionData.venue
      ? { label: productionData.venue.name, value: productionData.venue.id }
      : { label: "None", value: 0 },
  );
  const [author, setAuthor] = useState(
    productionData.author ? productionData.author : "",
  );
  const [description, setDescription] = useState(
    productionData.description ? productionData.description : "",
  );
  const [auditionDate, setAuditionDate] = useState(
    productionData.auditionDate ? new Date(productionData.auditionDate) : null,
  );
  const [sundowners, setSundowners] = useState(
    productionData.sundowners ? productionData.sundowners : false,
  );
  const [notConfirmed, setNotConfirmed] = useState(
    productionData.notConfirmed ? productionData.notConfirmed : false,
  );
  const [flyerFile, setFlyerFile] = useState(
    productionData.flyerFile ? productionData.flyerFile : "",
  );
  const [flyerImage, setFlyerImage] = useState(null);
  const [flyerPreviewUrl, setFlyerPreviewUrl] = useState(null);
  const [existingFlyerUrl, setExistingFlyerUrl] = useState(null);

  useEffect(() => {
    CloudinaryService.getUrl(productionData.id, "flyers")
      .then((response) => setExistingFlyerUrl(response.data.url))
      .catch(() => {});
  }, [productionData.id]);

  const [errorMessage, setErrorMessage] = useState("");

  const [descriptionLength, setDescriptionLength] = useState(
    productionData.description ? productionData.description.length : 0,
  );

  const handleSubmit = async (event) => {
    event.preventDefault();
    await handleEdit(event, productionData.id, name, venue ? venue.value : null, author, description, auditionDate, sundowners, notConfirmed, flyerFile);
    if (flyerImage) {
      await CloudinaryService.uploadImage(flyerImage, productionData.id, "flyers");
    }
  };

  const dataLoading = venues.isLoading || !productionData;

  if (dataLoading) return <CustomSpinner />;

  return (
    <div>
      <ErrorMessage message={errorMessage} />
      <form className="flex flex-col gap-2 max-w-md" onSubmit={handleSubmit}>
        <div>
          <div className="mb-2 block italic">
            <Label value="Production Name (required)" />
          </div>
          <TextInput
            placeholder="Oliver!"
            required
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        </div>
        <div>
          <div className="mb-2 block italic">
            <Label value="Venue" />
          </div>
          <Select
            options={venueOptions}
            isLoading={venues.isLoading}
            onChange={setVenue}
            className="w-full text-sm"
            styles={{
              control: (baseStyles) => ({
                ...baseStyles,
                borderRadius: 8,
                padding: 1,
              }),
            }}
            defaultValue={venue}
            isClearable
          />
        </div>
        <div>
          <div className="mb-2 block italic">
            <Label value="Author" />
          </div>
          <TextInput
            placeholder="William Shakespeare"
            value={author}
            onChange={(e) => setAuthor(e.target.value)}
          />
        </div>
        <div>
          <div className="mb-2 block italic">
            <Label
              value={`Description (max 2000 characters, current: ${descriptionLength})`}
            />
          </div>
          <Textarea
            placeholder="A tale of a young orphan... "
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            onBlur={(e) => setDescriptionLength(e.target.value.length)}
            rows={4}
          />
        </div>
        <div className="grid grid-cols-2">
          <div>
            <OptionalDateTimePicker
              label="Audition Date"
              value={auditionDate}
              onChange={setAuditionDate}
            />
          </div>
          <div className="flex flex-col justify-center gap-2">
            <div className="flex justify-center gap-2">
              <Label htmlFor="sundowners" className="flex italic">
                Sundowners?
              </Label>
              <Checkbox
                checked={sundowners}
                onChange={(e) => setSundowners(e.target.checked)}
              />
            </div>
            {/* <div className="col-span-1 flex justify-center gap-2">
              <Label htmlFor="sundowners" className="flex italic">
                Not Confirmed?
              </Label>
              <Checkbox
                checked={notConfirmed}
                onChange={(e) => setNotConfirmed(e.target.checked)}
              />
            </div> */}
          </div>
        </div>
        <div>
          <div className="mb-2 block italic">
            <Label value="Flyer" />
          </div>
          {flyerPreviewUrl || existingFlyerUrl ? (
            <img
              src={flyerPreviewUrl ?? existingFlyerUrl}
              alt="Flyer preview"
              className="max-h-48 rounded border border-gray-300 mb-2"
            />
          ) : (
            <div className="flex items-center justify-center h-32 border-2 border-dashed border-gray-300 rounded text-gray-400 text-sm mb-2">
              No flyer uploaded
            </div>
          )}
          <FileInput
            sizing="sm"
            onChange={(e) => {
              const file = e.target.files[0] ?? null;
              setFlyerImage(file);
              setFlyerPreviewUrl(file ? URL.createObjectURL(file) : null);
            }}
          />
        </div>
        <ConfirmCancelButtons handleCancel={() => setEditMode(false)} />
      </form>
    </div>
  );
};

export default EditProductionForm;