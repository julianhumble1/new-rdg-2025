import { AdvancedImage } from "@cloudinary/react"
import { PhotoIcon } from "@heroicons/react/16/solid"
import { FileInput } from "flowbite-react"
import { useState } from "react"
import CloudinaryService from "../../services/CloudinaryService.js"
import PersonService from "../../services/PersonService.js"
import {  Slide, ToastContainer, toast } from 'react-toastify';

const PersonImageWithUploadBox = ({ image, personData, fetchPersonData }) => {

    const [imageUpload, setImageUpload] = useState(false)

    const [file, setFile] = useState(null);

    const [imageSuccessMessage, setImageSuccessMessage] = useState("")
    const [imageErrorMessage, setImageErrorMessage] = useState("")

    const [personSuccessMessage, setPersonSuccessMessage] = useState("")
    const [personErrorMessage, setPersonErrorMessage] = useState("")

    const notifyPromise = () =>
        toast.promise(handleSubmitImage, {
            pending: "Uploading image...",
            success: imageSuccessMessage,
            error: imageErrorMessage,
        });

    const handleFileChange = (event) => {
        const selectedFile = event.target.files[0]
        setFile(selectedFile);
    }

    const handleSubmitImage = async () => {
        const formData = new FormData()
        formData.append("file", file)
        formData.append("upload_preset", "people")
        formData.append("public_id", personData.id)
        const signatureResponse = await getCloudinaryPersonSignature(personData.id)
        const { timestamp, signature, apiKey } = signatureResponse.data;
        formData.append("signature", signature)
        formData.append("api_key", apiKey)
        formData.append("timestamp", timestamp)
        const imageResponse = await submitImageToCloudinary(formData)
        if (imageResponse) {
            await updatePersonImageId(imageResponse.data.public_id)
        }
        setImageUpload(false)
        fetchPersonData()
    }

    const getCloudinaryPersonSignature = async () => {
        try {
            const response = await CloudinaryService.getSignature(personData.id, "people")
            console.log(response)
            return response
        } catch (e) {
            setImageErrorMessage(e.message)
        }
    }

    const submitImageToCloudinary = async (formData) => {
        try {
            const response = await CloudinaryService.uploadImage(formData)
            setImageSuccessMessage("Successfully uploaded image!")
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

    return (
        <div className=" border-slate-700 rounded-lg w-fit p-3 pb-0 text-center">
            <AdvancedImage cldImg={image} className="max-w-48 max-h-48 rounded border-4 border-white"/>
            <button className="font-bold text-sm hover:underline w-fit pt-2"  onClick={() => setImageUpload(!imageUpload)}>
                <div className="flex">
                    <PhotoIcon className="mr-2 h-5 w-5"/>
                    <div>
                        Upload image
                    </div>
                </div>
            </button>
            {imageUpload &&
                <div>
                    <FileInput onChange={handleFileChange} sizing="sm" />
                    <button className={`font-bold text-sm hover:underline ${!file && "cursor-not-allowed"}`} onClick={notifyPromise}>
                        Upload
                    </button>
                </div>
            }
            <ToastContainer
                position="bottom-right"
                autoClose={5000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick={false}
                rtl={false}
                pauseOnFocusLoss
                pauseOnHover
                theme="light"
                transition={Slide}
            />
        </div>
    )
}

export default PersonImageWithUploadBox