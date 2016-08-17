package com.marcosbarbero.kalah.web.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcosbarbero.kalah.dto.StartGameDTO;
import com.marcosbarbero.kalah.exception.GameFinishedException;
import com.marcosbarbero.kalah.helper.Given;
import com.marcosbarbero.kalah.model.entity.GameBoard;
import com.marcosbarbero.kalah.model.entity.enums.PlayerId;
import com.marcosbarbero.kalah.model.entity.enums.SpotId;
import com.marcosbarbero.kalah.service.GameService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for GameBoardController.
 *
 * @author Marcos Barbero
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameBoardControllerTest {


    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");
    MockMvc mockMvc;
    @MockBean
    private GameService service;

    @Autowired
    private ResponseExceptionHandler exceptionHandler;

    private JacksonTester<StartGameDTO> json;

    @Before
    public void setUp() {
        GameBoardController controller = new GameBoardController(this.service);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(this.exceptionHandler)
                .apply(documentationConfiguration(this.restDocumentation)).build();
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void testPostGameBoard() throws Exception {
        int stones = 6;
        StartGameDTO startGameDTO = Given.startGame(stones);

        GameBoard gameBoard = Given.gameBoard(stones, UUID.randomUUID().toString());
        given(this.service.create((any(Integer.class)))).willReturn(gameBoard);

        ResultActions result = this.mockMvc.perform(post(GameBoardController.URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json.write(startGameDTO).getJson()));
        result.andExpect(status().isCreated())
                .andDo(document("{method-name}",
                        requestFields(
                                fieldWithPath("stones").description("The number of stones per spot in the game")
                        ),
                        responseHeaders(headerWithName("Location").description("The created resource location"))
                ));
    }

    @Test
    public void testGetGameBoard() throws Exception {
        int stones = 6;
        String gameId = UUID.randomUUID().toString();
        GameBoard gameBoard = Given.gameBoard(stones, gameId);
        given(this.service.get(gameId)).willReturn(gameBoard);

        this.mockMvc.perform(get(GameBoardController.URI + "/{gameId}", gameId))
                .andExpect(status().isOk())
                .andDo(
                        document("{method-name}",
                                pathParameters(
                                        parameterWithName("gameId").description("Random UUID with gameId representation")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("The gameId, e.g: d3a719c4-3e5c-4d5f-abff-dd595e0aaabc").type(STRING),

                                        fieldWithPath("firstPlayer.id").description("The first player id, e.g: PLAYER_1").type(STRING),
                                        fieldWithPath("firstPlayer.spots[]").description("A list of spots"),
                                        fieldWithPath("firstPlayer.spots[].id").description("The spotId, e.g: SPOT_1").type(STRING),
                                        fieldWithPath("firstPlayer.spots[].stones").description("The number of stones in the spot, e.g: 6").type(NUMBER),
                                        fieldWithPath("firstPlayer.house.stones").description("The number of stones in the house, e.g: 32").type(NUMBER),

                                        fieldWithPath("secondPlayer.id").description("The second player id, e.g: PLAYER_2").type(STRING),
                                        fieldWithPath("secondPlayer.spots[]").description("A list of spots"),
                                        fieldWithPath("secondPlayer.spots[].id").description("The spotId, e.g: SPOT_1").type(STRING),
                                        fieldWithPath("secondPlayer.spots[].stones").description("The number of stones in the spot, e.g: 6").type(NUMBER),
                                        fieldWithPath("secondPlayer.house.stones").description("The number of stones in the house, e.g: 32").type(NUMBER),


                                        fieldWithPath("currentPlayer").description("The currentPlayer id representation, e.g: PLAYER_2").type(STRING)
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type of the result entity (application/json by default)")
                                )
                        )
                );
    }


    @Test
    public void testMoveGameBoard() throws Exception {
        int stones = 6;
        String gameId = UUID.randomUUID().toString();
        GameBoard gameBoard = Given.gameBoard(stones, gameId);
        given(this.service.move(gameId, SpotId.SPOT_1)).willReturn(gameBoard);

        this.mockMvc.perform(post(GameBoardController.URI + "/{gameId}/move/{spotId}", gameId, SpotId.SPOT_1))
                .andExpect(status().isOk())
                .andDo(
                        document("{method-name}",
                                pathParameters(
                                        parameterWithName("gameId").description("Random UUID with gameId representation"),
                                        parameterWithName("spotId").description("SpotId representation, available values: SPOT_1 to SPOT_6")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("The gameId, e.g: d3a719c4-3e5c-4d5f-abff-dd595e0aaabc").type(STRING),

                                        fieldWithPath("firstPlayer.id").description("The first player id, e.g: PLAYER_1").type(STRING),
                                        fieldWithPath("firstPlayer.spots[]").description("A list of spots"),
                                        fieldWithPath("firstPlayer.spots[].id").description("The spotId, e.g: SPOT_1").type(STRING),
                                        fieldWithPath("firstPlayer.spots[].stones").description("The number of stones in the spot, e.g: 6").type(NUMBER),
                                        fieldWithPath("firstPlayer.house.stones").description("The number of stones in the house, e.g: 32").type(NUMBER),

                                        fieldWithPath("secondPlayer.id").description("The second player id, e.g: PLAYER_2").type(STRING),
                                        fieldWithPath("secondPlayer.spots[]").description("A list of spots"),
                                        fieldWithPath("secondPlayer.spots[].id").description("The spotId, e.g: SPOT_1").type(STRING),
                                        fieldWithPath("secondPlayer.spots[].stones").description("The number of stones in the spot, e.g: 6").type(NUMBER),
                                        fieldWithPath("secondPlayer.house.stones").description("The number of stones in the house, e.g: 32").type(NUMBER),


                                        fieldWithPath("currentPlayer").description("The currentPlayer id representation, e.g: PLAYER_2").type(STRING)
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type of the result entity (application/json by default)")
                                )
                        )
                );
    }


    @Test
    public void testShowWinner() throws Exception {
        String gameId = UUID.randomUUID().toString();
        given(this.service.move(gameId, SpotId.SPOT_1)).willThrow(new GameFinishedException(gameId, PlayerId.PLAYER_1, 54));

        this.mockMvc.perform(post(GameBoardController.URI + "/{gameId}/move/{spotId}", gameId, SpotId.SPOT_1))
                .andExpect(status().isOk())
                .andDo(
                        document("{method-name}",
                                pathParameters(
                                        parameterWithName("gameId").description("Random UUID with gameId representation"),
                                        parameterWithName("spotId").description("SpotId representation, available values: SPOT_1 to SPOT_6")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("GameFinished").type(STRING),
                                        fieldWithPath("message").description("A message with the winner playerId, gameId and the number of stones").type(STRING)
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type of the result entity (application/json by default)")
                                )
                        )
                );
    }


}
