import React, { useState, useEffect } from 'react';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

const Test = () => {
    const [stompClient, setStompClient] = useState(null);

    useEffect(() => {
        const socket = new SockJS('http://localhost:8080/ws');
        const stomp = Stomp.over(socket);

        stomp.connect({}, () => {
            setStompClient(stomp);
            stomp.subscribe('/topic/test', (message) => {
                console.log(message);
                stomp.send("/app/test",
                    {},
                    "Ping");
            });
            stomp.send("/app/test", {"Authorization": `Bearer ${localStorage.token}`}, "Ping");
        });

        return () => {
            if (stompClient) {
                stompClient.disconnect();
            }
        };
    }, []);

    return <div></div>;
};

export default Test;
