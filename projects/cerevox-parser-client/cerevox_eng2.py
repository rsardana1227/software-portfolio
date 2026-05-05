import requests
import os

class CerevoxParser:
    def __init__(self, api_key):
        self.api_key = api_key
        self.base_url = "https://data.cerevox.ai/v0/elements"

    def partition(self, filepath):
        """
        Sends file to Cerevox services.

        Args:
            filepath (str): Path to the file to be sent.

        Returns:
            dict: Response from the Cerevox service.
        """
        try:
            with open(filepath, 'rb') as file:
                files = {'files': file.read()}
                headers = {'cerevox-api-key': self.api_key}
                response = requests.post(self.base_url, files=files, headers=headers)

                if response.status_code == 200:
                    return response.json()
                else:
                    raise Exception(f"Failed to send data to Cerevox: {response.status_code}")
        except FileNotFoundError:
            return {"error": "File not found."}
        except Exception as e:
            return {"error": str(e)}

def main():
    api_key = os.getenv("CEREVOX_API_KEY")
    filepath = os.getenv("CEREVOX_FILEPATH")

    if not api_key:
        raise RuntimeError("Set the CEREVOX_API_KEY environment variable before running this script.")
    if not filepath:
        raise RuntimeError("Set the CEREVOX_FILEPATH environment variable to the file you want to process.")

    cerevox_parser = CerevoxParser(api_key)
    response = cerevox_parser.partition(filepath)
    print(response)


if __name__ == "__main__":
    main()
