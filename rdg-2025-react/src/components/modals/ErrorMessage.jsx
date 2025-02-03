const ErrorMessage = ({ message }) => {
    if (message) {
        return (<div className="text-red-500">{message}</div>)
    }
}

export default ErrorMessage