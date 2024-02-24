import classes from "./User.module.css";

import React, {useEffect, useRef, useState} from "react";

import {TextField, Typography} from "@mui/material";
import {LocalizationProvider} from '@mui/x-date-pickers/LocalizationProvider';
import {AdapterDayjs} from '@mui/x-date-pickers/AdapterDayjs';
import {DateCalendar} from '@mui/x-date-pickers/DateCalendar';
import CustomTable from "../components/CustomTable";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import {toast, ToastContainer} from "react-toastify";
import {LineChart} from "@mui/x-charts";
import dayjs from "dayjs";
import Ribbon from "../components/Ribbon";

const User = () => {
    const noErrorString = " ";

    const [deviceList, setDeviceList] = useState([]);
    const [userName, setUserName] = useState("");
    const [deviceHourlyNotificationList, setDeviceHourlyNotificationList] = useState([]);
    const [stompClient, setStompClient] = useState(null);
    const [selectedDate, setSelectedDate] = useState(dayjs());
    const [deviceHourlyData, setDeviceHourlyData] = useState([]);
    const [deviceUuidError, setDeviceUuidError] = useState(noErrorString);

    const deviceUuidRef = useRef(null);

    const hoursList = Array.from({length: 25}, (_, index) => {
        const date = new Date();
        date.setHours(index);
        date.setMinutes(0);
        date.setSeconds(0);
        date.setMilliseconds(0);
        return date;
    });

    const devicesURL = "http://localhost:8080/devices";
    const usersURL = "http://localhost:8080/users";

    const deviceUuidEmptyErrorString = "Device UUID cannot be empty";

    const columnsDevices = [
        {field: "uuid", headerName: "UUID", flex: 1},
        {field: "description", headerName: "Description", flex: 1},
        {field: "address", headerName: "Address", flex: 1},
        {field: "maxWh", headerName: "Max Wh", flex: 1},
    ];

    const columnsDevicesNotifications = [
        {field: "id", headerName: "ID", flex: 1},
        {field: "uuid", headerName: "Device UUID", flex: 1},
        {field: "timestamp", headerName: "Timestamp", flex: 1},
        {field: "totalEnergyConsumption", headerName: "Total energy consumption (Wh)", flex: 1}
    ];

    useEffect(() => {
        stompClientConnect();

        fetchData();

        return () => {
            if (stompClient) {
                stompClient.disconnect();
            }
        };

    }, []);

    const fetchData = () => {
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
                    setUserName(data.name)
                    fetch(`${devicesURL}?userUuid=${data.uuid}`, {
                        headers: {
                            "Authorization": `Bearer ${localStorage.token}`,
                        }
                    })
                        .then((response) => {
                            console.log(response);
                            return response.json();
                        })
                        .then((data2) => {
                            console.log(data2.deviceDTOs);
                            setDeviceList(data2.deviceDTOs);
                        });
                },
                (error) => {
                    console.log(error);
                }
            );
    };

    const stompClientConnect = () => {
        const socket = new SockJS('http://localhost:8082/ws');
        const stomp = Stomp.over(socket);

        stomp.connect({}, () => {
            setStompClient(stomp);
            stomp.subscribe('/topic/notifications', energyExceededNotificationCallback);
            stomp.subscribe('/topic/graphData', graphDataCallback);
        });
    }

    const warn = (text) => toast.warn(text, {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "colored",
    });

    const isNotificationForUser = (userUuid) => {
        return localStorage.getItem("userUuid") === userUuid;
    }

    const energyExceededNotificationCallback = (message) => {
        let deviceHourlyNotification = JSON.parse(message.body);
        deviceHourlyNotification.totalEnergyConsumption = parseFloat(deviceHourlyNotification.totalEnergyConsumption.toFixed(2));
        deviceHourlyNotification.timestamp = new Date(deviceHourlyNotification.timestamp).toLocaleString();

        if (isNotificationForUser(message.headers.userUuid)) {
            console.log(deviceList);
            setDeviceHourlyNotificationList((previousList) => {
                return [...previousList, deviceHourlyNotification];
            });

            console.log(deviceHourlyNotification);

            warn(`Device with ID ${deviceHourlyNotification.uuid} has exceeded maximum energy consumption limit at ${deviceHourlyNotification.timestamp}!`);
        }
    }

    const addMissingHours = (array) => {
        const firstTimestamp = new Date(array[0].timestamp);
        const lastTimestamp = new Date(array[array.length - 1].timestamp);
        const missingHours = [];

        // Set the firstTimestamp to 00:00:00
        firstTimestamp.setHours(0, 0, 0, 0);

        // Set the lastTimestamp to 00:00:00 of the next day
        lastTimestamp.setHours(24, 0, 0, 0);

        for (let i = new Date(firstTimestamp); i < lastTimestamp; i.setHours(i.getHours() + 1)) {
            const timestamp = i.toISOString();
            const existingData = array.find(data => data.timestamp === timestamp);

            if (!existingData) {
                missingHours.push({
                    uuid: array[0].uuid,
                    timestamp,
                    totalEnergyConsumption: null
                });
            }
        }

        return array.concat(missingHours);
    };


    const sortGraphData = (array) => {
        return array.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));
    }

    const graphDataCallback = (message) => {
        const deviceHourlyMeasurements = JSON.parse(message.body);
        if (deviceHourlyMeasurements.userUuid === localStorage.getItem("userUuid")) {
            const graphDataArray = deviceHourlyMeasurements.deviceHourlyMeasurementDTOS;
            console.log(graphDataArray);
            setDeviceHourlyData(graphDataArray);
        }
    }

    const deviceRowHandler = (params) => {
        deviceUuidRef.current.value = params.row.uuid;
        graphGetHandler(selectedDate, params.row.uuid);
    }

    const handleDateChange = (date) => {
        setSelectedDate(date);
        graphGetHandler(date, deviceUuidRef.current.value);
    }

    const isGraphFormValid = (formData) => {
        setDeviceUuidError(noErrorString);

        let dataValid = true;

        if (formData.deviceUuid === "") {
            setDeviceUuidError(deviceUuidEmptyErrorString);
            dataValid = false;
        }
        if (formData.date === dayjs()) {
            dataValid = false;
        }

        return dataValid;
    }

    const graphGetHandler = (receivedDate, receivedDeviceUuid) => {
        const graphForm = {
            date: receivedDate.toISOString().split('T')[0],
            deviceUuid: receivedDeviceUuid,
            userUuid: localStorage.getItem("userUuid")
        }

        console.log(graphForm);

        if (isGraphFormValid(graphForm)) {
            stompClient.send('/app/graph', {}, JSON.stringify(graphForm))
        }
    }

    return (
        <div className={classes.mainDiv}>
            <div className={classes.tableDiv}>
                <Ribbon
                    logoutButtonActive={true}
                    chatButtonActive={true}
                    toUserButtonActive={false}
                    toAdminButtonActive={true}
                />
                <ToastContainer
                    newestOnTop={false}
                    rtl={false}
                    pauseOnFocusLoss
                    draggable
                />
                <Typography
                    variant="h3"
                    marginBottom={1}
                >{`Hi, ${userName}`}</Typography>
                <CustomTable
                    title="Device energy limit exceeded notifications"
                    height="50%"
                    rows={deviceHourlyNotificationList.map((deviceHourlyNotification, index) => ({
                        ...deviceHourlyNotification,
                        id: index
                    }))}
                    columns={columnsDevicesNotifications}
                    autoPageSize={true}
                />
                <CustomTable
                    title="Devices"
                    height="50%"
                    rows={deviceList}
                    columns={columnsDevices}
                    getRowId={(row) => row.uuid}
                    autoPageSize={true}
                    onRowClick={deviceRowHandler}
                />
                <div className={classes.graphDiv}>
                    <div className={classes.graphFieldsDiv}>
                        <TextField
                            label='Device UUID (select from table)'
                            id='deviceUuid'
                            type='text'
                            margin='dense'
                            size="small"
                            required
                            inputRef={deviceUuidRef}
                            helperText={deviceUuidError}
                            error={deviceUuidError !== noErrorString}
                            InputLabelProps={{shrink: true}}
                        />
                        <Typography
                            variant="p"
                            marginTop={1}
                        >Selected date:</Typography>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <DateCalendar
                                label="Selected date"
                                value={selectedDate}
                                onChange={handleDateChange}
                            />
                        </LocalizationProvider>
                    </div>
                    <LineChart
                        xAxis={[{data: hoursList, scaleType: "time"}]}
                        series={[
                            {
                                data: deviceHourlyData.map(deviceData => deviceData.totalEnergyConsumption > 0 ? deviceData.totalEnergyConsumption : null),
                                color: `#c9c900`,
                                area: true
                            },
                        ]}
                    />
                </div>
            </div>
        </div>
    );
};

export default User;
