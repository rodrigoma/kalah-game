$(document).ready(function() {
    load();
});

var startGame = function() {
    var noOfStones = {stones: $('#stones').val()}
    $.ajax({
        url: 'http://localhost:9090/games',
        data: JSON.stringify(noOfStones),
        method: 'POST',
        contentType: 'application/json',
        success: function (res, status, jqXHR) {
            $('#gameLocation').val(jqXHR.getResponseHeader('Location'));
            getGame(jqXHR.getResponseHeader('Location'));
        }
    });
};

var getGame = function(gameLocation) {
    $.ajax({
        url: gameLocation,
        method: 'GET',
        success: function(data) {
            fillData(data);
        }
    });
};

var makeAMove = function(spotId) {
    var resource = $('#gameLocation').val() + '/move/' + spotId;
    $.ajax({
        url: resource,
        method: 'POST',
        success: function(data) {
            fillData(data);
        }
    });
};

var fillData = function(data) {
    if(!data.currentPlayer) {
        $('#content').html('<div class="alert alert-success" role="alert">'+ data.message +'</div>');
        return;
    }
    $('#currentPlayer').text(data.currentPlayer);
    fillSpots('pl1Spot', data.firstPlayer.spots, data.firstPlayer.id != data.currentPlayer);
    fillSpots('pl2Spot', data.secondPlayer.spots, data.secondPlayer.id != data.currentPlayer);
    fillHouse('1', data.firstPlayer);
    fillHouse('2', data.secondPlayer)
};

var fillHouse = function(index, player) {
    $('#house' + index).text(player.house.stones);
};

var fillSpots = function(idPrefix, spots, disabled) {
    $.each(spots, function(index, spot) {
        var elementId = '#' + idPrefix + index;
        $(elementId).text(spot.stones);
        if(!disabled) {
            $(elementId).attr('onclick', "makeAMove('" + spot.id + "');")
        }
        $(elementId).prop("disabled", disabled);
    });
};

var load = function() {
    var gameLocation = $('#gameLocation').val();
    if (!gameLocation) {
        startGame();
    } else {
        getGame(gameLocation);
    }
};