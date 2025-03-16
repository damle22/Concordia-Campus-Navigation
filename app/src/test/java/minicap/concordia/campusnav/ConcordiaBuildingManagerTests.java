package minicap.concordia.campusnav;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager;
import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.entities.BuildingFloor;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;


public class ConcordiaBuildingManagerTests {

    @Test
    public void BuildingManager_GetCampus_ReturnsCampus(){
        ConcordiaBuildingManager manager = ConcordiaBuildingManager.getInstance();

        String expectedCampusName = "Sir George William campus";
        List<BuildingName> expectedBuildingNames = new ArrayList<>(Arrays.asList(
                BuildingName.HALL, BuildingName.MOLSON_SCHOOL_OF_BUSINESS
        ));
        float expectedLatitude = 45.49701f;
        float expectedLongitude = -73.57877f;

        Campus fetchedCampus = manager.getCampus(CampusName.SGW);

        Assert.assertEquals(expectedCampusName, fetchedCampus.getCampusName());
        Assert.assertEquals(expectedBuildingNames, fetchedCampus.getAssociatedBuildings());
        Assert.assertEquals(expectedLatitude, fetchedCampus.getLocation()[0], 0.00001);
        Assert.assertEquals(expectedLongitude, fetchedCampus.getLocation()[1], 0.00001);
    }

    @Test
    public void BuildingManager_GetBuilding_ReturnsCorrectBuilding(){
        ConcordiaBuildingManager manager = ConcordiaBuildingManager.getInstance();

        String expectedBuildingName = "Vanier library building";
        String expectedDescription = "The library is named after Major-General the Right Honourable Georges Philias Vanier, distinguished lawyer, soldier, diplomat, and Governor-General of Canada 1959-67. Vanier was a Loyola graduate (1906) and the recipient of the first Loyola Medal in 1963. In 1966 a 4.1-metre plaster replica of Michelangelo’s David was installed in the Vanier Library, a gift of Simpson's Department Store. It became a Loyola landmark and was the object of a number of student hi-jinks over the years, including painting it emerald green for St. Patrick‘s Day in 1967, and adorning it with fig leaves, hats, banana peels, and diapers.";
        CampusName expectedAssociatedCampus = CampusName.LOYOLA;
        int expectedNumberOfFloors = 3;
        float expectedLatitude = 45.45891f;
        float expectedLongitude = -73.63888f;

        Building actualBuilding = manager.getBuilding(BuildingName.VANIER_LIBRARY);

        Assert.assertEquals(expectedBuildingName, actualBuilding.getBuildingName());
        Assert.assertEquals(expectedDescription, actualBuilding.getDescription());
        Assert.assertEquals(expectedAssociatedCampus, actualBuilding.getAssociatedCampus());
        Assert.assertEquals(expectedNumberOfFloors, actualBuilding.getFloors().size());
        Assert.assertEquals(expectedLatitude, actualBuilding.getLocation()[0], 0.00001);
        Assert.assertEquals(expectedLongitude, actualBuilding.getLocation()[1], 0.00001);
    }

    @Test
    public void BuildingManager_GetBuildingsForCampus_ReturnsCorrectBuildings(){
        ConcordiaBuildingManager manager = ConcordiaBuildingManager.getInstance();

        int expectedNumberOfBuildings = 2;

        String[] expectedBuildingNames = new String[] { "Hall building", "John Molson School of Business"};
        String[] expectedDescriptions = new String[] {  "The Henry F. Hall Building is a high-density hub, located on De Maisonneuve Boulevard, on Concordia’s downtown Sir-George-Williams Campus.\nThe cube-like structure was completed in 1966. Its exterior is made of pre-fabricated, stressed concrete, a feature of the brutalist movement, often associated with French architect Le Corbusier.",
                                                        "In 2009 the John Molson Building officially opened on the corner of Guy and de Maisonneuve. It includes digitally equipped teaching amphitheatres and classrooms, faculty and graduate student offices, the Office of the Dean, student and faculty social space, as well as space for privatized programs. Special features also include case study rooms designed for group work, and laboratories for consumer behaviour research."};
        CampusName expectedAssociatedCampus = CampusName.SGW;
        int[] expectedNumberOfFloors = new int[] { 11, 17 };
        float[] expectedLatitudes = new float[] { 45.49701f, 45.45863f };
        float[] expectedLongitudes = new float[] { -73.57877f, -73.57906f };

        List<Building> actualBuildings = manager.getBuildingsForCampus(CampusName.SGW);

        Assert.assertEquals(expectedNumberOfBuildings, actualBuildings.size());

        for(int i = 0; i < actualBuildings.size(); i++) {
            Building currentBuilding = actualBuildings.get(i);

            Assert.assertEquals(expectedBuildingNames[i], currentBuilding.getBuildingName());
            Assert.assertEquals(expectedDescriptions[i], currentBuilding.getDescription());
            Assert.assertEquals(expectedAssociatedCampus, currentBuilding.getAssociatedCampus());
            Assert.assertEquals(expectedNumberOfFloors[i], currentBuilding.getFloors().size());
            Assert.assertEquals(expectedLatitudes[i], currentBuilding.getLocation()[0], 0.00001);
            Assert.assertEquals(expectedLongitudes[i], currentBuilding.getLocation()[1], 0.00001);
        }
    }

    @Test
    public void Building_GetFloors_ReturnsCorrectFloors(){
        ConcordiaBuildingManager manager = ConcordiaBuildingManager.getInstance();

        Building hallBuilding = manager.getBuilding(BuildingName.HALL);

        int expectedNumberOfFloors = 11;
        List<String> expectedFloorNames = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"));
        BuildingName expectedBuildingName = BuildingName.HALL;

        Collection<BuildingFloor> floors = hallBuilding.getFloors();

        Assert.assertEquals(expectedNumberOfFloors, floors.size());

        for(BuildingFloor floor:floors){
            Assert.assertTrue(expectedFloorNames.contains(floor.getFloorName()));
            Assert.assertEquals(expectedBuildingName, floor.getAssociatedBuilding());
        }
    }
}
