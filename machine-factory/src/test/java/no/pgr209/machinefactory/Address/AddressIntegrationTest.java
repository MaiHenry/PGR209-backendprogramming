package no.pgr209.machinefactory.Address;

import no.pgr209.machinefactory.service.DataFeedService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AddressIntegrationTest {

    @Autowired
    DataFeedService dataFeedService;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        dataFeedService.initializeData(); // Feed in-memory DB with sample data from DataFeedService.
    }

    @Test
    void shouldFetchAddresses() throws Exception {
        mockMvc.perform(get("/api/address"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].addressId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].addressId").value(2));
    }

    @Test
    void shouldFetchAddressOnPage() throws Exception {
        mockMvc.perform(get("/api/address/page/0"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].addressId").value(1));
    }

    @Test
    void shouldFetchAddressById() throws Exception {
        mockMvc.perform(get("/api/address/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressStreet").value("Storgata 33"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressCity").value("Oslo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressZip").value("2204"));
    }

    @Test
    void shouldCreateAddress() throws Exception {
        String addressJson = """
        {
            "addressStreet": "Kongens gate 15",
            "addressCity": "Oslo",
            "addressZip":  "0154"
        }
        """;

        // Create the address
        MvcResult createResult = mockMvc.perform(post("/api/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson))
                .andExpect(status().isCreated())
                .andReturn();

        // Extract the addressId from the response
        String responseContent = createResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(responseContent);
        int addressId = jsonObject.getInt("addressId");

        mockMvc.perform(get("/api/address/" + addressId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressId").value(addressId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressStreet").value("Kongens gate 15"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressCity").value("Oslo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressZip").value("0154"));
    }
}
