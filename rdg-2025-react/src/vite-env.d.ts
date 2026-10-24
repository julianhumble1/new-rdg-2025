/// <reference types="vite/client" />

interface ImportMetaEnv {
    readonly VITE_BACKEND_URL: string;
    readonly VITE_CLOUDINARY_BASE_FOLDER: string;
    readonly VITE_BEHOLD_WIDGET_ID: string;
}

interface ImportMeta {
    readonly env: ImportMetaEnv;
}