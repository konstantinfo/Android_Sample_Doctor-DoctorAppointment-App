package com.telemed.models.utils

import com.google.gson.annotations.SerializedName
import com.telemed.models.BaseModel

class GalleryModel: BaseModel(){
   //var sdcardPath: String? = null
   @SerializedName("sdcardPath")
   var sdcardPath: String? = null
}