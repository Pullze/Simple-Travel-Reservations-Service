import React, { useState, useEffect } from "react";
import { useLocation } from "react-router";
import {
  Layout,
  Row,
  Col,
  Form,
  Input,
  InputNumber,
  Select,
  DatePicker,
  TimePicker,
  Button,
  message,
} from "antd";
import moment from "moment";
import axios from "axios";

const { Option } = Select;

const today = moment();

const dateFormat = "YYYY-MM-DD";
const timeFormat = "HHmmss";

const fieldLabel = {
  flight_num: "Flight Number",
  airline_name: "Airline",
  from_airport: "From Airport",
  to_airport: "To Airport",
  departure_time: "Departure Time",
  arrival_time: "Arrival Time",
  flight_date: "Flight Date",
  cost: "Cost",
  capacity: "Capacity",
  current_date: "Current Date",
};

function ScheduleFlight() {
  const location = useLocation();
  const [form] = Form.useForm();
  const [airlines, setAirlines] = useState([]);
  const [flight, setFlight] = useState({ isScheduled: false });

  useEffect(() => {
    axios.get("/api/airlines").then((res) => setAirlines(res.data.data));
  }, []);

  const scheduleFlight = (values) => {
    const flight = {
      ...values,
      departure_time: values["departure_time"].format(timeFormat),
      arrival_time: values["arrival_time"].format(timeFormat),
      flight_date: values["flight_date"].format(dateFormat),
      current_date: today.format(dateFormat),
    };
    console.log("Success:", flight);
    const formData = new FormData();
    formData.append(
      "jsonValue",
      new Blob([JSON.stringify(flight)], { type: "application/json" })
    );
    axios
      .post("/api/schedule_flight", formData)
      .then((res) => {
        console.log(res);
        if (res.data.code === 200) {
          message.success("Successfully scheduled flight!");
          setTimeout(() => {
            setFlight({
              ...res.data.data,
              isScheduled: true,
            });
          }, 1000);
        } else {
          message.error(res.data.message);
        }
      })
      .catch((err) => console.log(err));
  };

  const handleClick = () => {
    window.location.reload();
  };

  const formItems = [
    {
      name: "flight_num",
      rules: [{ required: true, message: "Please enter the flight number." }],
      input: <InputNumber type="number" controls={false} />,
    },
    {
      name: "airline_name",
      rules: [{ required: true, message: "Please select the airline." }],
      input: (
        <Select>
          {airlines.map((airline, i) => (
            <Option key={i} value={airline.name}>
              {airline.name}
            </Option>
          ))}
        </Select>
      ),
    },
    {
      name: "from_airport",
      rules: [{ required: true, message: "Please enter the from airport ID." }],
      input: <Input />,
    },
    {
      name: "to_airport",
      rules: [
        { required: true, message: "Please enter the to airport ID." },
        ({ getFieldValue }) => ({
          validator(_, value) {
            if (!value || getFieldValue("from_airport") !== value) {
              return Promise.resolve();
            }
            return Promise.reject(
              new Error("To airport must not be the same as from airport.")
            );
          },
        }),
      ],
      input: <Input />,
    },
    {
      name: "departure_time",
      rules: [{ required: true, message: "Please enter the departure time." }],
      input: <TimePicker />,
    },
    {
      name: "arrival_time",
      rules: [{ required: true, message: "Please enter the arrival time." }],
      input: <TimePicker />,
    },
    {
      name: "flight_date",
      rules: [{ required: true, message: "Please enter the flight date." }],
      input: (
        <DatePicker
          disabledDate={(d) =>
            !d || d.format(dateFormat) <= today.format(dateFormat)
          }
        />
      ),
    },
    {
      name: "cost",
      rules: [
        { required: true, message: "Please enter the cost per person" },
        () => ({
          validator(_, value) {
            if (value === null || (value >= 0 && value <= 9999.99)) {
              return Promise.resolve();
            }
            return Promise.reject(
              new Error("Flight cost must be between 0.00 and 9999.99.")
            );
          },
        }),
      ],
      input: (
        <InputNumber
          addonBefore="$"
          addonAfter="per person"
          type="number"
          controls={false}
        />
      ),
    },
    {
      name: "capacity",
      rules: [
        { required: true, message: "Please enter the flight capacity." },
        () => ({
          validator(_, value) {
            console.log(value);
            if (value === null || value > 0) {
              return Promise.resolve();
            }
            return Promise.reject(
              new Error("Flight capacity must be greater than 0.")
            );
          },
        }),
      ],
      input: <InputNumber type="number" controls={false} />,
    },
    {
      name: "current_date",
      rules: [],
      input: <DatePicker defaultValue={today} disabled />,
    },
  ];

  if (flight.isScheduled) {
    return (
      <Layout style={{ minHeight: "100vh", padding: "10% 20%" }} align="middle">
        <Row justify="center" style={{ marginBottom: "5%" }}>
          <Col span={24}>
            <h1 className="heading">Flight Information</h1>
          </Col>
        </Row>
        {Object.entries(fieldLabel).map(([name, label], i) => (
          <Row key={i} style={{ margin: "0 30%" }} justify="center">
            <Col span={12} align="left">
              <p style={{ fontWeight: "bold" }}>{label}</p>
            </Col>
            <Col span={12} align="left">
              <p>{flight[name]}</p>
            </Col>
          </Row>
        ))}
        <Row style={{ marginTop: "5%" }}>
          <Col span={24}>
            <Button type="primary" onClick={handleClick}>
              Schedule another flight
            </Button>
          </Col>
        </Row>
      </Layout>
    );
  }

  return (
    <Layout>
      <Row style={{ minHeight: "100vh" }} align="middle">
        <Col span={24} align="middle">
          <h2>Now logged in as {location.state.email}</h2>
        </Col>
        <Col span={24} align="middle">
          <h1 className="heading">Schedule Flight</h1>
        </Col>
        <Col span={24} align="middle">
          <Form
            style={{ maxWidth: "300px" }}
            form={form}
            name="schedule-flight"
            onFinish={scheduleFlight}
            scrollToFirstError
          >
            {formItems.map((formItem, i) => (
              <Form.Item
                key={i}
                name={formItem.name}
                label={fieldLabel[formItem.name]}
                dependencies={
                  formItem.name === "to_airport" ? ["from_airport"] : []
                }
                rules={formItem.rules}
              >
                {formItem.input}
              </Form.Item>
            ))}
            <Row>
              <Col span={12}>
                <Form.Item>
                  <Button htmlType="reset">Cancel</Button>
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item>
                  <Button type="primary" htmlType="submit">
                    Schedule
                  </Button>
                </Form.Item>
              </Col>
            </Row>
          </Form>
        </Col>
      </Row>
    </Layout>
  );
}

export default ScheduleFlight;
