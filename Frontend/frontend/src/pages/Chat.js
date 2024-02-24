import classes from "./Chat.module.css";
import {
    Button,
    Collapse,
    Divider, IconButton,
    List,
    ListItemButton,
    ListItemIcon,
    ListItemText,
    ListSubheader, TextField,
    Typography
} from "@mui/material";
import React, {useEffect, useRef, useState} from "react";
import {ExpandLess, ExpandMore, StarBorder} from "@mui/icons-material";
import Ribbon from "../components/Ribbon";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import SendIcon from '@mui/icons-material/Send';
import {toast, ToastContainer} from "react-toastify";

const Chat = () => {
    const userRoleLoading = "Loading";
    const userRoleAdmin = "ADMIN";
    const userRoleUser = "USER";
    const todayDate = new Date();
    const noErrorString = "";

    var typing = false;

    const usersURL = 'http://localhost:8080/users';
    const chatWebsocketURL = 'http://localhost:8084/ws';
    const chatSendTopicString = "app";
    const chatReceiveTopicString = 'chat';
    const adminBroadcastString = 'adminBroadcast';
    const userBroadcastString = 'userBroadcast';
    const messagesFetchSendString = `/${chatSendTopicString}/messages`;
    const messagesFetchReceiveString = `/${chatReceiveTopicString}/messages`;
    const adminBroadcastAddress = `/${chatReceiveTopicString}/${adminBroadcastString}`;
    const adminBroadcastFetchSendString = `/${chatSendTopicString}/adminBroadcastMessages`;
    const adminBroadcastFetchReceiveString = `/${chatReceiveTopicString}/adminBroadcastMessages`;
    const selfSubscribeAddress = `/${chatReceiveTopicString}/${localStorage.username}`;
    const selfSubscribeNotificationAddress = `/${chatReceiveTopicString}/${localStorage.username}/notifications`;
    const notificationSendAddress = `/${chatSendTopicString}/seen`;
    const messageSendString = `/${chatSendTopicString}/send`;
    const adminBroadcastSendString = `/${chatSendTopicString}/sendAdminBroadcast`;
    const messageErrorString = 'The message cannot be empty!';
    // const chatSendFetchMessagesString = `/${chatSendTopicString}/messages`;
    // const chatReceiveFetchMessagesString = `${chatReceiveTopicString}/messages`;

    const [userRole, setUserRole] = useState(userRoleLoading);
    const [users, setUsers] = useState([]);
    const [admins, setAdmins] = useState([]);
    const [stompClient, setStompClient] = useState(null);
    const [openAdmins, setOpenAdmins] = useState(true);
    const [openUsers, setOpenUsers] = useState(true);
    const [messageBuffer, setMessageBuffer] = useState([]);
    const [secondUser, setSecondUser] = useState("");
    const [messageError, setMessageError] = useState(noErrorString);
    const [unseenList, setUnseenList] = useState([]);
    const [typingUser, setTypingUser] = useState(" ");

    const messageRef = useRef();

    useEffect(() => {
        stompClientConnect();

        return () => {
            if (stompClient) {
                stompClient.disconnect();
            }
        };
    }, []);

    const stompClientConnect = () => {
        const socket = new SockJS(chatWebsocketURL);
        const stomp = Stomp.over(socket);

        stomp.connect({}, () => {
            setStompClient(stomp);

            console.log(adminBroadcastAddress);

            stomp.subscribe(messagesFetchReceiveString, fetchMessagesCallback);
            stomp.subscribe(adminBroadcastFetchReceiveString, fetchMessagesCallback);
            stomp.subscribe(adminBroadcastAddress, messageReceivedCallback);
            stomp.subscribe(selfSubscribeAddress, messageReceivedCallback);
            stomp.subscribe(selfSubscribeNotificationAddress, notificationReceivedCallback);

            stomp.send(messagesFetchSendString, {}, JSON.stringify({senderOrReceiver: localStorage.username}));
            stomp.send(adminBroadcastFetchSendString, {}, JSON.stringify({senderOrReceiver: adminBroadcastString}));

            fetchData(stomp);
        });
    }

    const fetchMessagesCallback = (message) => {
        setMessageBuffer(prevMessageBuffer => [...prevMessageBuffer, ...JSON.parse(message.body).messageDTOs]);
    }

    const messageReceivedCallback = (message) => {
        const msg = JSON.parse(message.body);
        setMessageBuffer(prevMessageBuffer => [...prevMessageBuffer, msg])
        if (secondUser !== msg.sender) {
            setUnseenList(prevUnseenList => [...prevUnseenList, msg.sender]);
        }
    }

    const notificationReceivedCallback = (message) => {
        const notification = JSON.parse(message.body);
        console.log(notification);

        if (notification.notification === "SEEN") {
            info(notification.sender + " has seen your message!");
        } else if (notification.notification === "TYPING") {
            setTypingUser(notification.sender);
        } else if (notification.notification === "NOT_TYPING"){
            setTypingUser(" ");
        }
    }

    const info = (text) => toast.info(text, {
        position: "bottom-left",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "colored",
    });

    const fetchData = (stomp) => {
        fetch(`${usersURL}/${localStorage.username}`, {
            headers: {
                "Authorization": `Bearer ${localStorage.token}`,
            },
        })
            .then((response) => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error(response);
            })
            .then(
                (data) => {
                    setUserRole(data.userRole);
                    // if (data.userRole === "ADMIN") {
                    fetch(`${usersURL}/role?userRole=ADMIN`, {
                        headers: {
                            "Authorization": `Bearer ${localStorage.token}`,
                        },
                    })
                        .then((response) => {
                            if (response.ok) {
                                return response.json();
                            }
                            throw new Error(response);
                        })
                        .then(
                            (data) => {
                                setAdmins(data.userDTOs);
                            },
                            (error) => {
                                console.log(error);
                            }
                        );
                    // }
                    fetch(`${usersURL}/role?userRole=USER`, {
                        headers: {
                            "Authorization": `Bearer ${localStorage.token}`,
                        },
                    })
                        .then((response) => {
                            if (response.ok) {
                                return response.json();
                            }
                            throw new Error(response);
                        })
                        .then(
                            (data) => {
                                setUsers(data.userDTOs);
                            },
                            (error) => {
                                console.log(error);
                            }
                        );
                },
                (error) => {
                    console.log(error);
                }
            )
    }

    // const fetchAdminBroadcasts = () => {
    //     stompClient.send(chatSendFetchMessagesString, {}, JSON.stringify({senderOrReceiver: adminBroadcastString}));
    // }
    //
    //
    // const fetchMessages = (secondUsername) => {
    //     stompClient.send(chat)
    // }
    //
    // const fetchMessagesCallback = (message) => {
    //     console.log(message);
    // }

    const handleClickAdmins = () => {
        setOpenAdmins(!openAdmins);
    }

    const handleClickUsers = () => {
        setOpenUsers(!openUsers);
    }

    const handleClickAdminBroadcast = () => {
        setSecondUser(adminBroadcastString);
    }

    const handleListClick = (user) => {
        // console.log(event);
        if (user.username !== secondUser) {
            setSecondUser(user.username);
            if (unseenList.includes(user.username)) {
                stompClient.send(notificationSendAddress, {}, JSON.stringify({
                    sender: localStorage.username,
                    receiver: user.username,
                    notification: "SEEN"
                }));
                setUnseenList(prevUnseenList => prevUnseenList.filter((unseenElem) => unseenElem !== user.username));
            }
        }
    }

    const isMessageFormValid = (messageForm) => {
        setMessageError(noErrorString);

        if (messageForm.content === "" || messageForm.sender === "" || messageForm.receiver === "") {
            setMessageError(messageErrorString);
            return false;
        }

        return true;
    }

    const handleSendMessage = () => {
        const messageForm = {
            sender: localStorage.username,
            receiver: secondUser,
            content: messageRef.current.value,
        }
        if (isMessageFormValid(messageForm)) {
            messageRef.current.value = "";
            stompClient.send(messageSendString, {}, JSON.stringify(messageForm));
        }
    }

    const handleSendAdminBroadcast = () => {
        let messageForm = {};
        if (userRole === userRoleAdmin) {
            messageForm = {
                sender: adminBroadcastString,
                receiver: userBroadcastString,
                content: messageRef.current.value,
            }
        } else {
            messageForm = {
                sender: userBroadcastString,
                receiver: adminBroadcastString,
                content: messageRef.current.value,
            }
        }
        if (isMessageFormValid(messageForm)) {
            messageRef.current.value = "";
            stompClient.send(adminBroadcastSendString, {}, JSON.stringify(messageForm));
        }
    }

    const messageFilter = (message) => {
        // console.log(message);
        return (message.sender === localStorage.username && message.receiver === secondUser) ||
            (message.sender === secondUser && message.receiver === localStorage.username) ||
            (message.sender === adminBroadcastString && message.receiver === userBroadcastString) ||
            (message.sender === userBroadcastString && message.receiver === adminBroadcastString);
    }

    const messageMap = (message, i) => {
        // console.log(message);
        const messageDate = new Date(message.timestamp);
        const timestampText = todayDate.getDate() === messageDate.getDate() ?
            messageDate.toLocaleTimeString() :
            messageDate.toLocaleString();


        if (message.sender === localStorage.username ||
            (userRole === userRoleAdmin && message.sender === adminBroadcastString) ||
            (userRole === userRoleUser && message.sender === userBroadcastString)) {
            return <ListItemText
                key={i}
                primary={message.content}
                secondary={timestampText}
                primaryTypographyProps={{style: {color: `white`}}}
                secondaryTypographyProps={{style: {color: `white`}}}
                className={`${classes.message} ${classes.selfMessage}`}
            />;
        } else {
            return <ListItemText
                key={i}
                primary={message.sender + ': ' + message.content}
                secondary={timestampText}
                className={`${classes.message} ${classes.otherUserMessage}`}
            />;
        }
    }

    const listToItemMapper = (user) => {
        console.log(user);
        return (
            <ListItemButton sx={{pl: 4}} onClick={() => handleListClick(user)}>
                <ListItemText primary={user.username} primaryTypographyProps={{id: user.username}}/>
            </ListItemButton>
        );
    }

    const checkForTyping = () => {
        if (!typing && messageRef.current.value !== "") {
            stompClient.send(notificationSendAddress, {}, JSON.stringify({
                sender: localStorage.username,
                receiver: secondUser,
                notification: "TYPING"
            }));
            typing = true;
        } else if (typing && messageRef.current.value === "") {
            stompClient.send(notificationSendAddress, {}, JSON.stringify({
                sender: localStorage.username,
                receiver: secondUser,
                notification: "NOT_TYPING"
            }));
            typing = false;
        }
    }

    if (userRole === userRoleLoading) {
        return (
            <div className={classes.loadingDiv}>
                <Typography variant='h3'> Loading... </Typography>
            </div>
        );
    } else {
        return (
            <div className={classes.mainDiv}>
                <div className={classes.sidebarDiv}>
                    <List>
                        <ListItemButton onClick={handleClickAdmins}>
                            <ListItemText primary="Admins"/>
                            {openAdmins ? <ExpandLess/> : <ExpandMore/>}
                        </ListItemButton>
                        <Collapse in={openAdmins} timeout="auto">
                            <List component="div" disablePadding>
                                <ListItemButton sx={{pl: 4}} onClick={handleClickAdminBroadcast}>
                                    <ListItemText primary="Admin Broadcast"/>
                                </ListItemButton>
                                {admins.map(listToItemMapper)}
                            </List>
                        </Collapse>
                        <ListItemButton onClick={handleClickUsers}>
                            <ListItemText primary="Users"/>
                            {openUsers ? <ExpandLess/> : <ExpandMore/>}
                        </ListItemButton>
                        <Collapse in={openUsers} timeout="auto">
                            <List component="div" disablePadding>
                                {users.map(listToItemMapper)}
                            </List>
                        </Collapse>
                    </List>
                </div>
                <Divider sx={{bgcolor: "#606060"}} orientation='vertical'/>
                <div className={classes.chatDiv}>
                    <div>
                        <Ribbon
                            logoutButtonActive={true}
                            chatButtonActive={false}
                            toUserButtonActive={true}
                            toAdminButtonActive={true}
                        />
                        <ToastContainer
                            newestOnTop={false}
                            rtl={false}
                            pauseOnFocusLoss
                            draggable
                        />
                        <List sx={{maxHeight: '85vh', overflow: 'auto'}}>
                            {messageBuffer.filter(messageFilter).map(messageMap)}
                        </List>
                    </div>
                    <div className={classes.bottomBarDiv}>
                        {typingUser === secondUser && <Typography variant="p">Typing...</Typography>}
                        <div className={classes.messageTextFieldDiv}>
                            <TextField
                                label='Type your message here...'
                                id='message'
                                type='text'
                                margin='dense'
                                fullWidth
                                onChange={checkForTyping}
                                inputRef={messageRef}
                                helperText={messageError}
                                error={messageError !== noErrorString}
                            />
                            <Button
                                className={classes.sendButton}
                                variant='contained'
                                endIcon={<SendIcon/>}
                                onClick={(secondUser === adminBroadcastString || secondUser === userBroadcastString) ? handleSendAdminBroadcast : handleSendMessage}
                            >Send</Button>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}
export default Chat;