package com.dicoding.soccer.db.model

import com.google.gson.annotations.SerializedName

data class Player(

	@field:SerializedName("strPlayer")
	val strPlayer: String? = null,

	@field:SerializedName("strTeam")
	val strTeam: String? = null,

	@field:SerializedName("strTwitter")
	val strTwitter: String? = null,

	@field:SerializedName("strSigning")
	val strSigning: String? = null,

	@field:SerializedName("dateBorn")
	val dateBorn: String? = null,

	@field:SerializedName("strNationality")
	val strNationality: String? = null,

	@field:SerializedName("strGender")
	val strGender: String? = null,

	@field:SerializedName("strWeight")
	val strWeight: String? = null,

	@field:SerializedName("strInstagram")
	val strInstagram: String? = null,

	@field:SerializedName("strDescriptionEN")
	val strDescriptionEN: String? = null,

	@field:SerializedName("strFacebook")
	val strFacebook: String? = null,

	@field:SerializedName("idPlayer")
	val idPlayer: String? = null,

	@field:SerializedName("strBirthLocation")
	val strBirthLocation: String? = null,

	@field:SerializedName("strHeight")
	val strHeight: String? = null,

	@field:SerializedName("strPosition")
	val strPosition: String? = null,

	@field:SerializedName("strThumb")
	val strThumb: String? = null,

	@field:SerializedName("dateSigned")
	val dateSigned: String? = null
)