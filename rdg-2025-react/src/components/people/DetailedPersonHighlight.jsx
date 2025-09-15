import PersonImageWithUploadBox from "../photo_components/PersonImageWithUploadBox.jsx";
import ContactDetailsBox from "./ContactDetailsBox.jsx";
import Card from "../common/Card.jsx";

const DetailedPersonHighlight = ({
  personData,
  setEditMode,
  handleDelete,
  image,
  fetchPersonData,
}) => {
  const fullName = personData.firstName + " " + personData.lastName;

  if (personData)
    return (
      <div className="w-full flex justify-center">
        <Card className="flex flex-col sm:flex-row">
          <div className="flex flex-col">
            <div className="flex justify-center">
              <PersonImageWithUploadBox
                image={image}
                personData={personData}
                fetchPersonData={fetchPersonData}
              />
            </div>
            <ContactDetailsBox personData={personData} />
          </div>
          <div className="w-full flex flex-col">
            <div className="w-full h-fit flex sm:flex-row flex-col justify-between p-1">
              <div className="text-2xl font-bold flex justify-center">
                {fullName}
              </div>
              <div className="flex gap-2 font-bold justify-center">
                <button
                  className="text-sm hover:underline"
                  onClick={() => setEditMode(true)}
                >
                  Edit
                </button>
                <button
                  className="text-sm hover:underline"
                  onClick={() => handleDelete(personData)}
                >
                  Delete
                </button>
              </div>
            </div>
            <div className="p-1 text-sm">{personData.summary}</div>
          </div>
        </Card>
      </div>
    );
};

export default DetailedPersonHighlight;
