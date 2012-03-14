package plugins.grails.ukpostcodes

import grails.converters.JSON
import grails.converters.XML

class PostcodeService {

    def grailsApplication

    def lookup(postCode) {

        postCode = postCode?.trim().replaceAll(/[\\s]+/, '').toUpperCase()
        def foundPostCode = PostCode.findByCode(postCode)

        if (!foundPostCode) {
            try {
                def webResult = JSON.parse( new URL("http://www.uk-postcodes.com/postcode/${ postCode }.json").text )

                foundPostCode = new PostCode( code: postCode ,
                                latitude: webResult.geo.lat as float,
                                longitude: webResult.geo.lng as float,
                                constituency: webResult.administrative.constituency.title,
                                ward: webResult.administrative.ward.title,
                                district: webResult.administrative.district.title )

                foundPostCode.save()

            } catch (FileNotFoundException e) {
                log.error("Post code $postCode could not be found")
            }
        }

        foundPostCode

    }

}
