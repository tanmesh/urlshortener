import React, { useEffect, useState, useRef } from 'react';
import { Button } from 'react-bootstrap';
import { ImCancelCircle } from "react-icons/im";
import axios from 'axios';

function App() {
  const [longUrl, setLongUrl] = useState('')
  const [shortUrl, setShortUrl] = useState('')
  const [allUrls, setAllUrls] = useState([])
  const [alert, setAlert] = useState('')
  const inputRef = useRef(null);

  const baseUrl = process.env.REACT_APP_BASE_URL;

  useEffect(() => {
    console.log(baseUrl)
    if (alert) {
      const timeout = setTimeout(() => {
        setAlert('');
      }, 2000); // Duration in milliseconds (4 seconds)

      return () => clearTimeout(timeout);
    }
    // eslint-disable-next-line
  }, [alert]);

  const fetchData = () => {
    const config = {
      headers: {
        'Content-Type': 'application/json',
      },
    };

    axios.get(`${baseUrl}/tinyurl/getAll`, config)
      .then((response) => {
        setAllUrls(response.data);
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  };

  useEffect(() => {
    fetchData();
    // eslint-disable-next-line
  }, [shortUrl]);

  const handleButtonClicked = () => {
    if (longUrl === '' || longUrl === null) {
      setAlert('Please enter a valid URL')
      return;
    }

    const config = {
      headers: {
        'Content-Type': 'application/json',
      },
    };

    axios.post(`${baseUrl}/tinyurl/`, { 'longUrl': longUrl }, config)
      .then((response) => {
        setShortUrl(response.data)
        setLongUrl('')

        if (inputRef.current) {
          inputRef.current.value = '';
        }
      })
      .catch((error) => {
        setAlert(error.response.data)
        console.error("Error:", error);
      });
  }

  const handleDelete = (url) => {
    console.log(allUrls)
    setAllUrls(allUrls.filter((url_) => url_.shortUrl !== url))
    console.log(allUrls)

    const config = {
      headers: {
        'Content-Type': 'application/json',
      },
    };

    axios.delete(`${url}`, config)
      .then((response) => {
        setShortUrl('')

      })
      .catch((error) => {
        console.error("Error:", error);
      });
  }

  const handleMouseEnter = (event) => {
    event.target.style.backgroundColor = '#ddd';
  };

  const handleMouseLeave = (event) => {
    event.target.style.backgroundColor = '';
  };

  const sortedUrls = allUrls.slice().sort((a, b) => {
    return parseInt(b.timestamp) - parseInt(a.timestamp);
  });

  return (
    <div className='container'>
      <h3 className='title'>
        URL Shortener
      </h3>
      <div className='inputContainer'>
        {alert && <p style={{ color: 'red', fontWeight: 'bold' }}>{alert}</p>}
        <input
          type='text'
          onChange={(e) => setLongUrl(e.target.value)}
          placeholder='Paste your link here'
          className='inputStyle'
          ref={inputRef}
        />
        <div style={{ display: 'flex', flexDirection: 'row', justifyContent: 'center', marginTop: '10px' }}>
          <Button className='buttonStyle'
            onMouseEnter={handleMouseEnter}
            onMouseLeave={handleMouseLeave}
            onClick={() => handleButtonClicked()}>
            Shorten it!
          </Button>
        </div>
      </div>
      {sortedUrls.length !== 0 &&
        <div style={{ marginTop: '5%' }}>
          <table className="tableStyle">
            <thead>
              <tr style={{ backgroundColor: 'lightgrey' }}>
                <th className="tableCell">Long URL</th>
                <th className="tableCell">Short URL</th>
                <th className="tableCell">Creation Time</th>
              </tr>
            </thead>
            <tbody>
              {sortedUrls.map((url) => (
                <tr key={url.id}>
                  <td style={{ padding: '8px', textAlign: 'left', wordWrap: 'break-word' }}>
                    <a
                      href={url.longUrl}
                      target="_blank"
                      rel="noreferrer"
                      className='shortUrlStyle'>
                      {url.longUrl}
                    </a>
                  </td>
                  <td style={{ padding: '8px', textAlign: 'left', wordWrap: 'break-word' }}>
                    <a
                      href={url.shortUrl}
                      target="_blank"
                      rel="noreferrer"
                      className='shortUrlStyle'>
                      {url.shortUrl}
                    </a>
                  </td>
                  <td style={{ padding: '8px', textAlign: 'center', wordWrap: 'break-word' }}>
                    {url.timestamp ? new Date(parseInt(url.timestamp)).toLocaleString() : 'Invalid Timestamp'}
                  </td>
                  <td style={{ padding: '8px', textAlign: 'left', wordWrap: 'break-word' }}>
                    <ImCancelCircle
                      className='cancelButtonStyle'
                      onClick={() => handleDelete(url.shortUrl)}
                    />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      }
    </div >
  )
}

export default App;
