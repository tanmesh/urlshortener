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
    <div style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center' }}>
      <h3 className='title'>
        URL Shortener
      </h3>
      <div>
        {alert && <p style={{ color: 'red', fontWeight: 'bold' }}>{alert}</p>}
        <input
          type='text'
          onChange={(e) => setLongUrl(e.target.value)}
          placeholder='Paste your link here'
          className='inputStyle'
          ref={inputRef}
        />
        <Button className='buttonStyle'
          onMouseEnter={handleMouseEnter}
          onMouseLeave={handleMouseLeave}
          onClick={() => handleButtonClicked()}>
          Shorten it!
        </Button>
      </div>
      {sortedUrls.length !== 0 &&
        <div style={{ marginTop: '5%', display: 'flex', justifyContent: 'center' }}>
          <table style={{ borderCollapse: 'collapse', width: '90%', tableLayout: 'auto', margin: '0 auto' }}>
            <thead>
              <tr style={{ backgroundColor: 'lightgrey' }}>
                <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>Long URL</th>
                <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>Short URL</th>
                <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>Creation Time</th>
              </tr>
            </thead>
            <tbody>
              {sortedUrls.map((url) => (
                <tr key={url.id}>
                  <td style={{ padding: '8px', textAlign: 'left', wordWrap: 'break-word' }}>{url.longUrl}</td>
                  <td style={{ padding: '8px', textAlign: 'left', wordWrap: 'break-word' }}>
                    <a
                      href={url.shortUrl}
                      target="_blank"
                      rel="noreferrer"
                      style={{ textDecoration: 'none', color: 'blue' }}
                    >
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
