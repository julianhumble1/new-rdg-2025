import { Button, FileInput, Label } from "flowbite-react"
import { useState } from "react";
import ErrorMessage from "./modals/ErrorMessage.jsx";
import SuccessMessage from "./modals/SuccessMessage.jsx";
import CloudinaryService from "../services/CloudinaryService.js";

const CloudinaryUploadTest = () => {

    const [file, setFile] = useState(null);

    const [successMessage, setSuccessMessage] = useState("")
    const [errorMessage, setErrorMessage] = useState("")

    const handleFileChange = (event) => {
        const selectedFile = event.target.files[0]
        setFile(selectedFile);
    }

    const handleSubmit = async () => {
        const formData = new FormData()
        formData.append("file", file)
        formData.append("upload_preset", "people")
        console.log(file)

        try {
            const response = await CloudinaryService.uploadImage(formData)
            setSuccessMessage("Successfully uploaded")
        } catch (e) {
            setErrorMessage(e.message)
        }
    }

    return (
        <div>
            <SuccessMessage message={successMessage} />
            <ErrorMessage message={errorMessage} />
            <div className="mb-2 block">
                <Label htmlFor="file-upload" value="Upload file" />
            </div>
            <FileInput id="file-upload" onChange={handleFileChange} />
            
            <Button onClick={handleSubmit}>
                Upload
            </Button>
        </div>
    )
}

export default CloudinaryUploadTest