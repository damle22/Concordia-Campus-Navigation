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
        String expectedDescription = "The vanier library building";
        CampusName expectedAssociatedCampus = CampusName.LOYOLA;
        int expectedNumberOfFloors = 2;
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
        String[] expectedDescriptions = new String[] { "The Hall building", "The John Molson School of Business building"};
        CampusName expectedAssociatedCampus = CampusName.SGW;
        int[] expectedNumberOfFloors = new int[] { 4, 2 };
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

        int expectedNumberOfFloors = 4;
        List<String> expectedFloorNames = new ArrayList<>(Arrays.asList("1", "2", "8", "9"));
        BuildingName expectedBuildingName = BuildingName.HALL;

        Collection<BuildingFloor> floors = hallBuilding.getFloors();

        Assert.assertEquals(expectedNumberOfFloors, floors.size());

        for(BuildingFloor floor:floors){
            Assert.assertTrue(expectedFloorNames.contains(floor.getFloorName()));
            Assert.assertEquals(expectedBuildingName, floor.getAssociatedBuilding());
        }
    }
}
