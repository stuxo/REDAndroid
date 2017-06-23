package ch.redacted.app.test.common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.redacted.data.model.Announcement;
import ch.redacted.data.model.Profile;

/**
 * Factory class that makes instances of data models with random field values.
 * The aim of this class is to help setting up test fixtures.
 */
public class TestDataFactory {

    public static String randomUuid() {
        return UUID.randomUUID().toString();
    }

    //Example generators

    //public static Ribot makeRibot(String uniqueSuffix) {
    //    return Ribot.create(makeProfile(uniqueSuffix));
    //}
	//
    //public static List<Ribot> makeListRibots(int number) {
    //    List<Ribot> ribots = new ArrayList<>();
    //    for (int i = 0; i < number; i++) {
    //        ribots.add(makeRibot(String.valueOf(i)));
    //    }
    //    return ribots;
    //}

    public static Profile makeProfile() {
        Profile.Response response = new Profile.Response();
        //Create mocks here
        response.avatar = "";
        response.community = null;
        response.isFriend = false;
        response.personal = null;
        response.ranks = null;

        return Profile.ProfileBuilder.aProfile().withResponse(response)
                .build();
    }

    private static List<Announcement.Announcements> makeAnnouncements(int number) {

        List<Announcement.Announcements> announcements = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            Announcement.Announcements announcement = new Announcement.Announcements();
            //Create mocks here
            announcement.bbBody = "asdasd";
            announcement.body = "123-0dsafomifasf";
            announcement.newsId = 1;
            announcement.title = "seeotskaf1204-ewfs\n\n\n\n";

            announcements.add(announcement);
        }
        return announcements;
    }

    public static List<Announcement.Announcements> getAnnouncements(int number){
        Announcement.Response response = new Announcement.Response();


        response.announcements = makeAnnouncements(number);

        return Announcement.AnnouncementBuilder.aAnnouncement().withResponse(response)
                .build().response.announcements;
    }
}