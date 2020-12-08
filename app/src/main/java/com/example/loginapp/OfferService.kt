package com.example.loginapp

class OfferService {

    fun getOffers(): ArrayList<Offer> {
        val offers = ArrayList<Offer>()
        offers.add(
            Offer(
                "Barcelona 3 nights",
                "Barcelona had many venues for live music and theatre, including the world-renowned Gran Theatre del Liceu opera",
                300,
                R.drawable.offer_1
            )
        )
        offers.add(
            Offer(
                "Maldive 7 nights",
                "The first Maldivians did not leave any archaeological artifacts. Their buildings were probably built of wood, palm",
                500,
                R.drawable.offer_2
            )
        )
        offers.add(
            Offer(
                "Crete 5 nights",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
                400,
                R.drawable.offer_3
            )
        )
        return offers
    }
}