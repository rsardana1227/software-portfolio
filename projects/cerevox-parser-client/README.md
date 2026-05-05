# Cerevox Parser Client

Small Python client for sending a file to the Cerevox parsing API.

## Files
- `cerevox_eng2.py`
- `requirements.txt`

## Setup

```bash
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
export CEREVOX_API_KEY="your_api_key"
export CEREVOX_FILEPATH="/path/to/file.pdf"
python3 cerevox_eng2.py
```
