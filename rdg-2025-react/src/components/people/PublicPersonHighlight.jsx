import { AdvancedImage } from "@cloudinary/react"

const PublicPersonHighlight = ({ personData, image }) => {
    
    const fullName = personData.firstName + " " + personData.lastName

    return (
        <div className="w-full flex justify-center">
            <div className="bg-slate-200 md:w-4/5 w-full flex flex-col sm:flex-row p-2 sm:mt-2 rounded shadow-lg ">
                <div className="flex justify-center">
                    <AdvancedImage cldImg={image} className="max-w-48 max-h-48 rounded border-4 border-white" />
                </div>
                <div className="flex flex-col p-2 gap-1">
                    <div className="font-bold text-2xl text-center sm:text-start">
                        {fullName}
                    </div>
                    <div className="text-sm">
						{personData.summary}	
					</div>	
                </div>
            </div>
        </div>
    )
}

export default PublicPersonHighlight