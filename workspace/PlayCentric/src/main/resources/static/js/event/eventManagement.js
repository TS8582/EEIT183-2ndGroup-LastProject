$(document).ready(function() {
    // 加載所有活動列表
    loadEventList();

    // 點擊新增活動按鈕，打開模態框
    window.openCreateEventForm = function() {
        $('#eventModal').modal('show');
        $('#eventForm').trigger('reset');
        $('#eventId').val('');
        $('#deleteEventBtn').hide();  // 隱藏刪除按鈕
    };

    // 加載所有活動列表
    function loadEventList() {
        $.get('/events/find', function(events) {
            var eventList = $('#eventList');
            eventList.empty();
            $.each(events, function(index, event) {
                var row = '<tr>' +
                            '<td>' + event.eventName + '</td>' +
                            '<td>' + event.eventDescription + '</td>' +
                            '<td>' + new Date(event.eventStartTime).toLocaleString() + '</td>' +
                            '<td>' + new Date(event.eventEndTime).toLocaleString() + '</td>' +
                            '<td>' + new Date(event.eventSignupDeadLine).toLocaleString() + '</td>' +
                            '<td>' +
                                '<button class="btn btn-info btn-sm" onclick="openEditEventForm(' + event.eventId + ')">編輯</button>' +
                                '<button class="btn btn-danger btn-sm ml-1" onclick="deleteEvent(' + event.eventId + ')">刪除</button>' +
                            '</td>' +
                        '</tr>';
                eventList.append(row);
            });
        });
    }

    // 點擊保存按鈕，創建或更新活動
    window.saveEvent = function() {
        var formData = $('#eventForm').serialize();
        var url = $('#eventId').val() ? '/events/update' : '/events/create';
        $.ajax({
            type: 'POST',
            url: url,
            data: formData,
            success: function(event) {
                $('#eventModal').modal('hide');
                loadEventList();
            },
            error: function(error) {
                console.log('Error:', error);
            }
        });
    };

    // 點擊編輯按鈕，打開編輯模態框
    window.openEditEventForm = function(eventId) {
        $.get('/events/get/' + eventId, function(event) {
            $('#eventId').val(event.eventId);
            $('#eventName').val(event.eventName);
            $('#eventDescription').val(event.eventDescription);
            $('#eventStartTime').val(event.eventStartTime.split('.')[0]);
            $('#eventEndTime').val(event.eventEndTime.split('.')[0]);
            $('#eventSignupDeadLine').val(event.eventSignupDeadLine.split('.')[0]);
            $('#eventModal').modal('show');
            $('#deleteEventBtn').show();  // 顯示刪除按鈕
        });
    };

    // 點擊刪除按鈕，刪除活動
    window.deleteEvent = function(eventId) {
        if (confirm('確定要刪除這個活動嗎？')) {
            $.ajax({
                type: 'DELETE',
                url: '/events/delete/' + eventId,
                success: function() {
                    loadEventList();
                },
                error: function(error) {
                    console.log('Error:', error);
                }
            });
        }
    };

    // 查詢活動
//    window.searchEvents = function() {
//        var query = $('#searchEventInput').val();
//        $.get('/events/find?query=' + query, function(events) {
//            var eventList = $('#eventList');
//            eventList.empty();
//            $.each(events, function(index, event) {
//                var row = '<tr>' +
//                            '<td>' + event.eventName + '</td>' +
//                            '<td>' + event.eventDescription + '</td>' +
//                            '<td>' + new Date(event.eventStartTime).toLocaleString() + '</td>' +
//                            '<td>' + new Date(event.eventEndTime).toLocaleString() + '</td>' +
//                            '<td>' + new Date(event.eventSignupDeadLine).toLocaleString() + '</td>' +
//                            '<td>' +
//                                '<button class="btn btn-info btn-sm" onclick="openEditEventForm(' + event.eventId + ')">編輯</button>' +
//                                '<button class="btn btn-danger btn-sm ml-1" onclick="deleteEvent(' + event.eventId + ')">刪除</button>' +
//                            '</td>' +
//                        '</tr>';
//                eventList.append(row);
//            });
//        });
//    };
});
