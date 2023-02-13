package com.example.mujika

class CabangRestoranModel (
    private var nama_restoran: String,
    private var alamat_restoran: String,
    private var telp_restoran: String) {

    fun getNamaRestoran() : String {
        return nama_restoran
    }
    fun getAlamatRestoran() : String {
        return alamat_restoran
    }

    fun getTelpRestoran() : String {
        return telp_restoran
    }

    fun setNamaRestoran(nama_restoran: String) {
        this.nama_restoran = nama_restoran
    }

    fun setAlamatRestoran(alamat_restoran: String) {
        this.alamat_restoran = alamat_restoran
    }

    fun setTeleponRestoran(telp_restoran: String) {
        this.telp_restoran = telp_restoran
    }
}
