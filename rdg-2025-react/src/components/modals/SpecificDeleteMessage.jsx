import { useState, useEffect } from "react"

const SpecificDeleteMessage = ({ itemToDelete }) => {

    const [message, setMessage] = useState("")

    useEffect(() => {
        if (itemToDelete.postcode != null) {
            setMessage(<>
                    When deleting a venue, any <strong>performances</strong> associated with the venue will also be deleted.
                </>)
        } else if (itemToDelete.auditionDate != null) {
            setMessage(<>
                    When deleting a production, any <strong>performances</strong> associated with the venue will also be deleted.
                </>)
        } else if (itemToDelete.year != null) {
            setMessage(<>
                    When deleting a festival, <strong>performances</strong> associated with the festival will <strong>not</strong> be deleted.
                </>)
        } else if (itemToDelete.time != null) {
            setMessage(<>
                When deleting a performance, no other objects will be deleted.
            </>)
        }

    }, [itemToDelete])

    return (
        <div className="text-sm text-gray-600 mt-4">{ message }</div>
    )
}

export default SpecificDeleteMessage