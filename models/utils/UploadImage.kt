package com.telemed.models.utils
import android.graphics.Bitmap



class UploadImage {
    var id: String? = null         // id returnd from server after uploading or updating an photo
    var imageURL: String? = null // for server image
    var imageFilePath: String? = null // for device image
    var bigImageURL: String? = null // for instagram and twitter (we get original size of image in advance for these two)
    var position: Int = 0
    var mediaType: MediaType? = null
    var socialMediaId: String? = null // for facebook purpose (to get original size image)
    var bitmap: Bitmap? = null

    fun reset() {
        // don't reset the position value
        this.id = null
        this.imageURL = null
        this.imageFilePath = null
        this.bigImageURL = null
        this.socialMediaId = null
        this.mediaType = null
        this.bitmap = null
    }

    fun copyValueFrom(uploadImage: UploadImage) {
        // don't copy the position value
        this.id = uploadImage.id
        this.imageURL = uploadImage.imageURL
        this.imageFilePath = uploadImage.imageFilePath
        this.bigImageURL = uploadImage.bigImageURL
        this.socialMediaId = uploadImage.socialMediaId
        this.mediaType = uploadImage.mediaType
        this.bitmap = uploadImage.bitmap
    }
}
