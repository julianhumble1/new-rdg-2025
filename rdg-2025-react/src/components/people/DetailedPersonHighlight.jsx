import { format } from "date-fns"
import { Link } from "react-router-dom"
import AddressHelper from "../../utils/AddressHelper.js"
import { Button, FileInput, Label } from "flowbite-react"
import { PhotoIcon } from "@heroicons/react/16/solid"
import { useState } from "react"
import CloudinaryService from "../../services/CloudinaryService.js"
import SuccessMessage from "../modals/SuccessMessage.jsx"
import ErrorMessage from "../modals/ErrorMessage.jsx"
import PersonService from "../../services/PersonService.js"
import { AdvancedImage } from "@cloudinary/react"

const DetailedPersonHighlight = ({ personData, setEditMode, handleDelete , image}) => {
    
    const fullAddress = AddressHelper.getFullAddress(personData.addressStreet, personData.addressTown, personData.addressPostcode)

    const [imageUpload, setImageUpload] = useState(false)

    const [file, setFile] = useState(null);

    const [imageSuccessMessage, setImageSuccessMessage] = useState("")
    const [imageErrorMessage, setImageErrorMessage] = useState("")

    const [personSuccessMessage, setPersonSuccessMessage] = useState("")
    const [personErrorMessage, setPersonErrorMessage] = useState("")

    const handleFileChange = (event) => {
        const selectedFile = event.target.files[0]
        setFile(selectedFile);
    }

    const handleSubmitImage = async () => {
        const formData = new FormData()
        formData.append("file", file)
        formData.append("upload_preset", "people")
        console.log(file)
        const imageResponse = await submitImageToCloudinary(formData)
        if (imageResponse) {
            updatePersonImageId(imageResponse.data.public_id)
        }
    }

    const submitImageToCloudinary = async (formData) => {
        try {
            const response = await CloudinaryService.uploadImage(formData)
            setImageSuccessMessage("Successfully uploaded image")
            return response
        } catch (e) {
            setImageErrorMessage(e.message)
        }
    }

    const updatePersonImageId = async (imageId) => {
        try {
            const response = await PersonService.updatePersonWithImage(personData, imageId)
            console.log(response)
            setPersonSuccessMessage("Successfully saved image id to database")
        } catch (e) {
            setPersonErrorMessage(e.message)
        }
    }

    if (personData) return (
        <div className="bg-sky-900 bg-opacity-35 lg:w-1/2 md:w-2/3 w-full rounded p-4 mt-3 md:mx-0 m-3 flex md:flex-row flex-col shadow-md gap-2">
            {image &&
                <div className="flex md:w-fit  justify-center">
                    <AdvancedImage cldImg={image} className="max-w-48 max-h-48 rounded border-4 border-white"/>
                </div>
            }
            <div className=" flex flex-col gap-2 w-full">
                <div className="text-black text-xl font-bold flex justify-between">
                    <div>
                        {personData.firstName} {personData.lastName}
                    </div>
                    <div className="flex gap-2">

                        <button className="text-sm hover:underline" onClick={() => setEditMode(true)}>
                            Edit
                        </button>
                        <button className="text-sm hover:underline" onClick={() => handleDelete(personData)}>
                            Delete
                        </button>
                    </div>
                </div>
                {personData.summary &&
                    <div className="text-sm">
                        {personData.summary}
                    </div>    
                }
                {fullAddress &&
                    <div className="flex flex-col">
                        <div className="font-bold italic">
                            Address
                        </div>
                        <div>
                            {fullAddress}
                        </div>
                    </div>       
                }
                {personData.homePhone &&
                    <div className="flex flex-col">
                        <div className="font-bold italic">
                            Home
                        </div>
                        <div>
                            {personData.homePhone}
                        </div>
                    </div>    
                }
                {personData.mobilePhone &&
                    <div className="flex flex-col">
                        <div className="font-bold italic">
                            Mobile
                        </div>
                        <div>
                            {personData.mobilePhone}
                        </div>
                    </div>    
                }
                <div>
                    <div className="flex text-sm gap-1">
                        <div className="font-bold italic">
                            Created:
                        </div>    
                        <div>
                            {format(new Date(personData.createdAt), "dd-MM-yyyy")}
                        </div>
                    </div>
                    <div className="flex text-sm gap-1">
                        <div className="font-bold italic">
                            Last Updated:
                        </div>    
                        <div>
                            {format(new Date(personData.updatedAt), "dd-MM-yyyy")}
                        </div>
                    </div>  
                </div>
                <div className="flex justify-between">
                    <button className="font-bold text-sm hover:underline w-fit"  onClick={() => setImageUpload(true)}>
                        <div className="flex">
                            <PhotoIcon  className="mr-2 h-5 w-5"/>
                            <div>
                                Upload image
                            </div>
                        </div>
                    </button>
                    <Link to="/people" className="text-sm hover:underline font-bold text-end">
                        See All People
                    </Link>
                </div>

                {imageUpload &&
                    <div>
                        <FileInput id="file-upload" onChange={handleFileChange} />
                        <SuccessMessage message={imageSuccessMessage} />
                        <ErrorMessage message={imageErrorMessage} />
                        <SuccessMessage message={personSuccessMessage} />
                        <ErrorMessage message={personErrorMessage} />

                        <Button onClick={handleSubmitImage} className={`${!file && "cursor-not-allowed"}`}>
                            Upload
                        </Button>
                    </div>
                }

            </div>

        </div>
    )
}

export default DetailedPersonHighlight